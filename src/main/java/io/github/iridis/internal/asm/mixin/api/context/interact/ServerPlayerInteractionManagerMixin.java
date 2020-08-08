package io.github.iridis.internal.asm.mixin.api.context.interact;

import static io.github.iridis.api.context.ContextKey.of;
import static io.github.iridis.api.context.ContextManager.INSTANCE;

import io.github.iridis.api.context.ContextManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

@Mixin (ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
	@Shadow public ServerPlayerEntity player;

	@Shadow public ServerWorld world;

	@Redirect (method = "interactBlock",
			at = @At (value = "INVOKE",
					target = "Lnet/minecraft/item/ItemStack;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;"))
	private ActionResult useOnBlock(ItemStack stack, ItemUsageContext context) {
		ContextManager.getInstance().pushStackMarker();
		ActionResult result = ContextManager.getInstance().act(() -> stack.useOnBlock(context),
		             of("interactContext", context),
		             of("interactPos", context.getBlockPos()),
		             of("interactHand", context.getHand()),
		             of("exactInteractPos", context.getHitPos()),
		             of("interactPlayer", context.getPlayer()),
		             of("interactItem", context.getStack()));
		ContextManager.getInstance().popStackMarker();
		return result;
	}

	@Redirect (method = "interactBlock",
			at = @At (value = "INVOKE",
					target = "Lnet/minecraft/block/BlockState;onUse(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;"))
	private ActionResult useOnBlock(BlockState state, World world, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ContextManager.getInstance().pushStackMarker();
		ActionResult result = ContextManager.getInstance().act(() -> state.onUse(world, player, hand, hit),
		                                   of("useContext", hit),
		                                   of("usePos", hit.getBlockPos()),
		                                   of("useHand", hand),
		                                   of("usePlayer", player));
		ContextManager.getInstance().popStackMarker();
		return result;
	}
}
