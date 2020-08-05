package io.github.iridis.internal.asm.mixin.events.lifecycle;

import io.github.iridis.internal.events.LifecycleEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import v1_16_1.net.minecraft.server.IMinecraftServer;

import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin_ServerStartEvent {
	@Inject(method = "runServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;getMeasuringTimeMs()J", ordinal = 0))
	private void runServer(CallbackInfo ci) {
		LifecycleEvents.serverTick((IMinecraftServer) this);
	}
}
