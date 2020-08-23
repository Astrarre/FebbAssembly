package io.github.iridis.internal.events;

import io.github.iridis.internal.asm.mixin.events.lifecycle.MinecraftServerMixin_ServerStartEvent;
import io.github.iridis.internal.asm.mixin.events.lifecycle.MinecraftServerMixin_ServerStopEvent;
import io.github.iridis.internal.asm.mixin.events.lifecycle.MinecraftServerMixin_ServerTickEvent;
import io.github.iridis.internal.events.annotations.ModPresent;
import io.github.iridis.internal.events.annotations.SingleMixin;
import io.github.iridis.internal.events.eams.ModEventApplyManager;
import net.devtech.nanoevents.api.Logic;
import net.devtech.nanoevents.api.annotations.Invoker;
import v1_16_1.net.minecraft.server.IMinecraftServer;

@SuppressWarnings ("ReferenceToMixin")
public class LifecycleEvents {
	@Invoker (value = "iridis:server_tick", args = {
			ModPresent.class,
			SingleMixin.class
	}, applyManager = ModEventApplyManager.class)
	@ModPresent ({
			"fabric-events-lifecycle-v1",
			"fabric-events-lifecycle-v0"
	})
	@SingleMixin (MinecraftServerMixin_ServerTickEvent.class)
	public static void serverTick(IMinecraftServer server) {
		Logic.start();
		serverTick(server);
		Logic.end();
	}

	@Invoker (value = "iridis:server_start", args = {
			ModPresent.class,
			SingleMixin.class
	}, applyManager = ModEventApplyManager.class)
	@ModPresent ({
			"fabric-events-lifecycle-v1",
			"fabric-events-lifecycle-v0"
	})
	@SingleMixin (MinecraftServerMixin_ServerStartEvent.class)
	public static void serverStart(IMinecraftServer server) {
		Logic.start();
		serverStart(server);
		Logic.end();
	}

	@Invoker (value = "iridis:server_stop", args = {
			ModPresent.class,
			SingleMixin.class
	}, applyManager = ModEventApplyManager.class)
	@ModPresent ({
			"fabric-events-lifecycle-v1",
			"fabric-events-lifecycle-v0"
	})
	@SingleMixin (MinecraftServerMixin_ServerStopEvent.class)
	public static void serverStop(IMinecraftServer server) {
		Logic.start();
		serverStop(server);
		Logic.end();
	}
}
