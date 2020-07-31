package io.github.iridis.internal.invokers;

import net.devtech.nanoevents.api.Logic;
import net.devtech.nanoevents.api.annotations.Invoker;
import v1_16_1.net.minecraft.server.IMinecraftServer;

public class LifecycleEvents {
	@Invoker("iridis:server_tick")
	public static void serverTick(IMinecraftServer server) {
		Logic.start();
		serverTick(server);
		Logic.end();
	}

	@Invoker("iridis:server_start")
	public static void serverStart(IMinecraftServer server) {
		Logic.start();
		serverStart(server);
		Logic.end();
	}

	@Invoker("iridis:server_stop")
	public static void serverStop(IMinecraftServer server) {
		Logic.start();
		serverStop(server);
		Logic.end();
	}
}
