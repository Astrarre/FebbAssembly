package io.github.iridis.internal.events;

import io.github.iridis.internal.events.annotations.ModPresent;
import net.devtech.nanoevents.api.Logic;
import net.devtech.nanoevents.api.annotations.Invoker;
import v1_16_1.net.minecraft.server.IMinecraftServer;

public class LifecycleEvents {
	@Invoker (value = "iridis:server_tick", args = ModPresent.class)
	@ModPresent ({
			"fabric-events-lifecycle-v1",
			"fabric-events-lifecycle-v0"
	})
	public static void serverTick(IMinecraftServer server) {
		Logic.start();
		serverTick(server);
		Logic.end();
	}

	@Invoker (value = "iridis:server_start", args = ModPresent.class)
	@ModPresent ({
			"fabric-events-lifecycle-v1",
			"fabric-events-lifecycle-v0"
	})
	public static void serverStart(IMinecraftServer server) {
		Logic.start();
		serverStart(server);
		Logic.end();
	}

	@Invoker (value = "iridis:server_stop", args = ModPresent.class)
	@ModPresent ({
			"fabric-events-lifecycle-v1",
			"fabric-events-lifecycle-v0"
	})
	public static void serverStop(IMinecraftServer server) {
		Logic.start();
		serverStop(server);
		Logic.end();
	}
}
