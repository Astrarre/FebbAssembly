package io.github.iridis.internal.asm.mixin.events.block;

import io.github.iridis.internal.events.BlockEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import v1_16_1.net.minecraft.item.IItemPlacementContext;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

@Mixin (BlockItem.class)
public class BlockItemMixin_PreBlockPlaceEvent {
	@Inject (method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
			at = @At (value = "INVOKE", target = "Lnet/minecraft/item/ItemPlacementContext;getBlockPos()Lnet/minecraft/util/math/BlockPos;"),
			cancellable = true)
	private void prePlaceBlock(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
		if (BlockEvents.preBlockPlaceEvent((IItemPlacementContext) context)) {
			PlayerEntity entity = context.getPlayer();
			if (entity instanceof ServerPlayerEntity) {
				((ServerPlayerEntity) entity).onHandlerRegistered(entity.currentScreenHandler, entity.currentScreenHandler.getStacks());
			}
			cir.setReturnValue(ActionResult.FAIL);
		}
	}
}