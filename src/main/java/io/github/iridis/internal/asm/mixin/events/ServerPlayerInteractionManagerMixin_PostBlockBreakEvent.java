package io.github.iridis.internal.asm.mixin.events;

import io.github.iridis.internal.invokers.BlockEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import v1_16_1.net.minecraft.block.IBlockState;
import v1_16_1.net.minecraft.block.entity.IBlockEntity;
import v1_16_1.net.minecraft.entity.player.IPlayerEntity;
import v1_16_1.net.minecraft.util.math.IBlockPos;
import v1_16_1.net.minecraft.world.IWorld;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin_PostBlockBreakEvent {
	@Shadow public ServerPlayerEntity player;

	@Shadow public ServerWorld world;

	@Inject (method = "tryBreakBlock",
	         at = @At (value = "INVOKE",
	                   target = "Lnet/minecraft/block/Block;afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/item/ItemStack;)V"),
	         locals = LocalCapture.CAPTURE_FAILHARD,
	         cancellable = true)
	private void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir, BlockState state, BlockEntity entity) {
		BlockEvents.playerPostBreak((IPlayerEntity) this.player, (IWorld) this.world, (IBlockPos)pos, (IBlockState)state, (IBlockEntity)entity);
	}
}
