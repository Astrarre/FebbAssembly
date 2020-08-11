/*
 * Copyright 2016 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.loader.launch.knot;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.entrypoint.minecraft.hooks.EntrypointUtils;
import net.fabricmc.loader.game.GameProvider;
import net.fabricmc.loader.game.GameProviders;
import net.fabricmc.loader.launch.common.FabricLauncherBase;
import net.fabricmc.loader.launch.common.FabricMixinBootstrap;
import net.fabricmc.loader.util.UrlConversionException;
import net.fabricmc.loader.util.UrlUtil;
import org.spongepowered.asm.launch.MixinBootstrap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * I just copied Knot and pasted it here
 */
public final class Knot2 extends FabricLauncherBase {
	protected Map<String, Object> properties = new HashMap<>();

	private KnotClassLoaderInterface classLoader;
	private EnvType envType;
	private GameProvider provider;

	public Knot2(EnvType type) {
		this.envType = type;
	}

	public ClassLoader init(String[] args) {
		setProperties(this.properties);

		List<GameProvider> providers = GameProviders.create();
		this.provider = null;

		for (GameProvider p : providers) {
			if (p.locateGame(this.envType, this.getClass().getClassLoader())) {
				this.provider = p;
				break;
			}
		}

		if (this.provider != null) {
			LOGGER.info("Loading for game " + this.provider.getGameName() + " " + this.provider.getRawGameVersion());
		} else {
			LOGGER.error("Could not find valid game provider!");
			for (GameProvider p : providers) {
				LOGGER.error("- " + p.getGameName()+ " " + p.getRawGameVersion());
			}
			throw new RuntimeException("Could not find valid game provider!");
		}

		this.provider.acceptArguments(args);
		boolean useCompatibility = this.provider.requiresUrlClassLoader() || Boolean.parseBoolean(System.getProperty("fabric.loader.useCompatibilityClassLoader", "false"));
		this.classLoader = useCompatibility ? new KnotCompatibilityClassLoader(this.isDevelopment(), this.envType, this.provider) : new KnotClassLoader(this.isDevelopment(),
		                                                                                                                                                this.envType,
		                                                                                                                                                this.provider);
		ClassLoader cl = (ClassLoader) this.classLoader;
		if (this.provider.isObfuscated()) {
			for (Path path : this.provider.getGameContextJars()) {
				FabricLauncherBase.deobfuscate(this.provider.getGameId(), this.provider.getNormalizedGameVersion(), this.provider.getLaunchDirectory(),
				                               path,
				                               this
				);
			}
		}
		// Locate entrypoints before switching class loaders
		this.provider.getEntrypointTransformer().locateEntrypoints(this);
		Thread.currentThread().setContextClassLoader(cl);
		@SuppressWarnings("deprecation")
		FabricLoader loader = FabricLoader.INSTANCE;
		loader.setGameProvider(this.provider);
		loader.load();
		loader.freeze();
		FabricLoader.INSTANCE.getAccessWidener().loadFromMods();
		MixinBootstrap.init();
		FabricMixinBootstrap.init(this.getEnvironmentType(), loader);
		FabricLauncherBase.finishMixinBootstrapping();
		this.classLoader.getDelegate().initializeTransformers();
		EntrypointUtils.invoke("preLaunch", PreLaunchEntrypoint.class, PreLaunchEntrypoint::onPreLaunch);
		return cl;
	}

	@Override
	public String getTargetNamespace() {
		return "named";
	}

	@Override
	public Collection<URL> getLoadTimeDependencies() {
		String cmdLineClasspath = System.getProperty("java.class.path");

		return Arrays.stream(cmdLineClasspath.split(File.pathSeparator)).filter((s) -> {
			if (s.equals("*") || s.endsWith(File.separator + "*")) {
				System.err.println("WARNING: Knot does not support wildcard classpath entries: " + s + " - the game may not load properly!");
				return false;
			} else {
				return true;
			}
		}).map((s) -> {
			File file = new File(s);
			try {
				return (UrlUtil.asUrl(file));
			} catch (UrlConversionException e) {
				LOGGER.debug(e);
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toSet());
	}

	@Override
	public void propose(URL url) {
		FabricLauncherBase.LOGGER.debug("[Knot] Proposed " + url + " to classpath.");
		this.classLoader.addURL(url);
	}

	@Override
	public EnvType getEnvironmentType() {
		return this.envType;
	}

	@Override
	public boolean isClassLoaded(String name) {
		return this.classLoader.isClassLoaded(name);
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		try {
			return this.classLoader.getResourceAsStream(name, false);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read file '" + name + "'!", e);
		}
	}

	@Override
	public ClassLoader getTargetClassLoader() {
		return (ClassLoader) this.classLoader;
	}

	@Override
	public byte[] getClassByteArray(String name, boolean runTransformers) throws IOException {
		if (runTransformers) {
			return this.classLoader.getDelegate().getPreMixinClassByteArray(name, false);
		} else {
			return this.classLoader.getDelegate().getRawClassByteArray(name, false);
		}
	}

	@Override
	public boolean isDevelopment() {
		return true;
	}

	@Override
	public String getEntrypoint() {
		return this.provider.getEntrypoint();
	}
}
