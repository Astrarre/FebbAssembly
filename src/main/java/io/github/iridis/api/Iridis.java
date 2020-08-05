package io.github.iridis.api;

import io.github.iridis.internal.compat.FabricApi;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class Iridis implements ModInitializer {
	public static final boolean IN_DEV = FabricLoader.getInstance().isDevelopmentEnvironment();
	public static final boolean IS_CLIENT = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	public static final boolean IS_SERVER = !IS_CLIENT;

	@Override
	public void onInitialize() {
		FabricApi.init();
		// todo iridis mods entrypoint, load order?
	}
}
