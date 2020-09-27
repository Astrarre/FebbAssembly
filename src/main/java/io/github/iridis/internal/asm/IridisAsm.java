package io.github.iridis.internal.asm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.chocohead.mm.api.ClassTinkerers;
import com.google.common.collect.ImmutableMap;
import io.github.iridis.api.Iridis;
import io.github.iridis.internal.Constants;

public class IridisAsm implements Runnable {
	private static Properties getRuntimeManifest() {
		Properties manifest = new Properties();
		try (InputStream stream = IridisAsm.class.getClassLoader()
		                                         .getResourceAsStream("runtimeManifest.properties")) {
			if (stream == null) {
				throw new IllegalStateException("Fuck resources");
			}
			manifest.load(stream);
		} catch (IOException e) {
			throw new IllegalStateException("Could not load runtime manifest for attaching febb api interfaces!", e);
		}
		return manifest;
	}

	private static final Map<String, String> API_TO_MC;

	static {
		ImmutableMap.Builder<String, String> map = ImmutableMap.builder();
		getRuntimeManifest().forEach((k, v) -> {
			if (Iridis.IN_DEV) {
				String mapped = Constants.MAPPING_RESOLVER.mapClassName("intermediary", k.toString());
				map.put(v.toString(), (mapped == null ? k.toString() : mapped.replace('.', '/')));
			} else {
				map.put(v.toString(), k.toString());
			}
		});
		API_TO_MC = map.build();
	}

	@Override
	public void run() {
		System.out.println("Attaching Iridis API to Minecraft...");
		API_TO_MC.forEach((apiName, mcName) -> ClassTinkerers.addTransformation(mcName, clazz -> clazz.interfaces.add(apiName)));
		System.out.println("Iridis transformation complete!");
	}

	/**
	 * only works with interface methods
	 */
	public static String getMinecraftFromApi(String apiClassName) {
		return API_TO_MC.getOrDefault(apiClassName, apiClassName);
	}
}
