package io.github.iridis.internal.asm.mixin.events.block;

import io.github.iridis.internal.events.BlockEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import v1_16_1.net.minecraft.block.IBlockState;
import v1_16_1.net.minecraft.util.math.IBlockPos;
import v1_16_1.net.minecraft.world.IWorld;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

@Mixin (World.class)
public class WorldMixin_PostSetBlockEvent {
	@Inject (method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z",
			at = @At (value = "INVOKE",
					target = "Lnet/minecraft/world/World;onBlockChanged(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;" + "Lnet" +
					         "/minecraft/block/BlockState;)V"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	public void postChange(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir, WorldChunk chunk,
	                       Block block, BlockState oldState) {
		BlockEvents.postBlockSetEvent((IWorld) this, (IBlockPos) pos, (IBlockState) state, (IBlockState) oldState);
	}
}
