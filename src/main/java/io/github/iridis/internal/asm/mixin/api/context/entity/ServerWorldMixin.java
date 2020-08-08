package io.github.iridis.internal.asm.mixin.api.context.entity;

import io.github.iridis.api.context.ContextManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
	@Redirect(method = "tickEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;tick()V"))
	private void stack(Entity entity) {
		ContextManager.getInstance()
		              .pushStackMarker();
		ContextManager.getInstance()
		              .push(entity);
		entity.tick();
		ContextManager.getInstance()
		              .pop(entity);
		ContextManager.getInstance()
		              .popStackMarker();
	}
}
