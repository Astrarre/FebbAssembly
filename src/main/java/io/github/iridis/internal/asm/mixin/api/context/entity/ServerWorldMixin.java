package io.github.iridis.internal.asm.mixin.api.context.entity;

import io.github.iridis.api.context.DefaultContext;
import io.github.iridis.internal.asm.access.ContextHolderAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
	@Inject(method = "addEntity", at = @At("TAIL"))
	private void onAdd(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		((ContextHolderAccess)entity).setContext(DefaultContext.BLAME.get().copyStack());
	}

	@Redirect(method = "tickEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;tick()V"))
	private void stack(Entity entity) {
		DefaultContext.BLAME.get()
		              .pushStackMarker();
		DefaultContext.BLAME.get()
		              .push("tickEntity", entity);
		DefaultContext.BLAME.get().actStack(((ContextHolderAccess) entity).getContext(), () -> {
			entity.tick();
			return null;
		});
		DefaultContext.BLAME.get()
		              .pop("tickEntity", entity);
		DefaultContext.BLAME.get()
		              .popStackMarker();
	}
}
