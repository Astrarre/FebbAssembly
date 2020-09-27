package io.github.iridis.internal.asm.mixin.api.context.server;

import java.util.function.BooleanSupplier;

import io.github.iridis.api.context.DefaultContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

@Mixin(value = MinecraftServer.class, priority = Integer.MIN_VALUE) // we want to be the last tail so we don't end up with memory leaks
public class MinecraftServerMixin {

	@Inject (method = "runServer", at = @At("HEAD"))
	private void pushContext(CallbackInfo ci) {
		DefaultContext.SERVER.get().push("server", this);
		DefaultContext.SERVER.get().pushStackMarker();
	}

	@Inject (method = "runServer", at = @At("TAIL"))
	private void popContext(CallbackInfo ci) {
		DefaultContext.SERVER.get().popStackMarker();
		DefaultContext.SERVER.get().pop("server", this);
	}

	@Redirect(method = "tickWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V"))
	private void tick(ServerWorld world, BooleanSupplier shouldKeepTicking) {
		DefaultContext.SERVER.get().push("worldTick", world);
		DefaultContext.SERVER.get().pushStackMarker();
		world.tick(shouldKeepTicking);
		DefaultContext.SERVER.get().popStackMarker();
		DefaultContext.SERVER.get().pop("worldTick", world);
	}
}
