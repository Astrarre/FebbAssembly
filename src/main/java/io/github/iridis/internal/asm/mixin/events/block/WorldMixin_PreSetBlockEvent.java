package io.github.iridis.internal.asm.mixin.events.block;

import io.github.iridis.internal.events.BlockEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import v1_16_1.net.minecraft.block.IBlockState;
import v1_16_1.net.minecraft.util.math.IBlockPos;
import v1_16_1.net.minecraft.world.IWorld;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin (World.class)
public class WorldMixin_PreSetBlockEvent {
	@Inject (method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z",
			at = @At (value = "INVOKE",
					target = "Lnet/minecraft/world/chunk/WorldChunk;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)" +
					         "Lnet/minecraft/block/BlockState;"),
			cancellable = true)
	private void preChange(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
		if (BlockEvents.preBlockSetEvent((IWorld) this, (IBlockPos) pos, (IBlockState) state)) {
			cir.setReturnValue(false);
		}
	}
}
