package io.github.iridis;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class Iridis {
	public static final boolean IN_DEV = FabricLoader.getInstance().isDevelopmentEnvironment();
	public static final boolean IS_CLIENT = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	public static final boolean IS_SERVER = !IS_CLIENT;

}
