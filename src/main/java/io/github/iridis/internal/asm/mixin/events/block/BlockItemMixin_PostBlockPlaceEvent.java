package io.github.iridis.internal.asm.mixin.events.block;

import io.github.iridis.internal.events.BlockEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import v1_16_1.net.minecraft.item.IItemPlacementContext;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;

@Mixin (BlockItem.class)
public class BlockItemMixin_PostBlockPlaceEvent {
	@Inject (method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
			at = @At (value = "RETURN"),
			slice = @Slice (from = @At (value = "INVOKE", target = "Lnet/minecraft/util/ActionResult;success(Z)Lnet/minecraft/util/ActionResult;")),
			cancellable = true)
	private void prePlaceBlock(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
		BlockEvents.postBlockPlaceEvent((IItemPlacementContext) context);
	}
}