package io.github.iridis.internal.compat;

import io.github.iridis.internal.events.LifecycleEvents;
import v1_16_1.net.minecraft.server.IMinecraftServer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.event.server.ServerStopCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.fabricmc.loader.api.FabricLoader;

public class FabricApi {
	private static final FabricLoader LOADER = FabricLoader.getInstance();
	static {
		if(LOADER.isModLoaded("fabric-api-base")) {
			if(LOADER.isModLoaded("fabric-events-lifecycle-v1")) {
				ServerTickEvents.END_SERVER_TICK.register(m -> LifecycleEvents.serverTick((IMinecraftServer)m));
				ServerLifecycleEvents.SERVER_STARTED.register(m -> LifecycleEvents.serverTick((IMinecraftServer) m));
				ServerLifecycleEvents.SERVER_STOPPING.register(m -> LifecycleEvents.serverStop((IMinecraftServer) m));
			} else if(LOADER.isModLoaded("fabric-events-lifecycle-v0")) {
				ServerTickCallback.EVENT.register(m -> LifecycleEvents.serverTick((IMinecraftServer) m));
				ServerStartCallback.EVENT.register(m -> LifecycleEvents.serverStart((IMinecraftServer)m));
				ServerStopCallback.EVENT.register(m -> LifecycleEvents.serverStop((IMinecraftServer) m));
			}
		}
	}

	public static void init() {}
}
