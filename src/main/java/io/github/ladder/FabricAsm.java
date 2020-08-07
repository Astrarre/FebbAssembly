package io.github.ladder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.chocohead.mm.api.ClassTinkerers;

import net.fabricmc.loader.api.FabricLoader;

public class FabricAsm implements Runnable {


	@Override
	public void run() {

		boolean isDev = FabricLoader.getInstance()
		                            .isDevelopmentEnvironment();
		if (!isDev) {
			System.out.println("Attaching Ladder API to Minecraft...");

			final Properties mcToApi = loadMcToApi();

			mcToApi.forEach((intMcName, apiName) -> {
				String classNameInCurrentRuntime = intMcName.toString();

				ClassTinkerers.addTransformation(classNameInCurrentRuntime, (clazz) -> clazz.interfaces.add(apiName.toString()));
			});

			System.out.println("Ladder transformation complete!");
		}

	}

	private static Properties loadMcToApi() {
		Properties manifest = new Properties();
		try (InputStream stream = FabricAsm.class.getClassLoader()
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
}
