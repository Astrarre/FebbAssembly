package io.github.iridis.internal.asm.mixin.events.block;

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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin_PreBlockBreakEvent {
	@Shadow public ServerWorld world;

	@Shadow public ServerPlayerEntity player;

	@Inject (method = "tryBreakBlock",
	         at = @At (value = "INVOKE",
	                   target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V"),
	         locals = LocalCapture.CAPTURE_FAILHARD,
	         cancellable = true)
	private void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir, BlockState state, BlockEntity entity, Block block) {
		if (!BlockEvents.playerPreBreak((IPlayerEntity) this.player, (IWorld) this.world, (IBlockPos)pos, (IBlockState)state, (IBlockEntity)entity)) {
			BlockPos cornerPos = pos.add(-1, -1, -1);
			for (int x = 0; x < 3; x++) {
				for (int y = 0; y < 3; y++) {
					for (int z = 0; z < 3; z++) {
						this.player.networkHandler.sendPacket(new BlockUpdateS2CPacket(this.world, cornerPos.add(x, y, z)));
					}
				}
			}
			cir.setReturnValue(false);
		}
	}
}
