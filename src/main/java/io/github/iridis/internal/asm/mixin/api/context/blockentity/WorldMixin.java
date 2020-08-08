package io.github.iridis.internal.asm.mixin.api.context.blockentity;

import io.github.iridis.api.context.ContextManager;
import io.github.iridis.internal.asm.access.ContextHolderAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(World.class)
public class WorldMixin {
	@Inject(method = "setBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addBlockEntity(Lnet/minecraft/block/entity/BlockEntity;)Z"))
	private void context(BlockPos pos, @Nullable BlockEntity blockEntity, CallbackInfo ci) {
		if(blockEntity != null) {
			((ContextHolderAccess) blockEntity).setContext("iridis:placed_", ContextManager.getInstance()
			                                                                               .copyStack());
		}
	}

	@Redirect(method = "tickBlockEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Tickable;tick()V"))
	private void stack(Tickable tickable) {
		ContextManager.getInstance()
		              .pushStack(((ContextHolderAccess)tickable).getContext("iridis:placed_"), () -> {
			tickable.tick();
			return null;
		});
	}
}
