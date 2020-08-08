package io.github.iridis.internal.asm.mixin.api.context.server;

import java.util.function.BooleanSupplier;

import io.github.iridis.api.context.ContextManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.world.ServerWorld;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Inject (method = "runServer", at = @At("HEAD"))
	private void pushContext(CallbackInfo ci) {
		ContextManager.getInstance().push("server", this);
		ContextManager.getInstance().pushStackMarker();
	}

	@Inject (method = "runServer", at = @At("TAIL"))
	private void popContext(CallbackInfo ci) {
		ContextManager.getInstance().popStackMarker();
		ContextManager.getInstance().pop("server", this);
	}

	@Redirect(method = "tickWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V"))
	private void tick(ServerWorld world, BooleanSupplier shouldKeepTicking) {
		ContextManager.getInstance().push("worldTick", world);
		ContextManager.getInstance().pushStackMarker();
		world.tick(shouldKeepTicking);
		ContextManager.getInstance().popStackMarker();
		ContextManager.getInstance().pop("worldTick", world);
	}
}
