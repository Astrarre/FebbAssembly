package io.github.iridis.internal.asm.mixin.api.block;

import io.github.iridis.api.block.MoistBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import v1_16_1.net.minecraft.block.IBlockState;
import v1_16_1.net.minecraft.util.math.IBlockPos;
import v1_16_1.net.minecraft.world.IWorld;

import net.minecraft.block.Block;
import net.minecraft.block.CropBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@Mixin (CropBlock.class)
public class CropBlockMixin_MoistBlock {
	@ModifyVariable (method = "getAvailableMoisture", at = @At (value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"), ordinal = 1)
	private static float moist(float g, Block block, BlockView world, BlockPos pos) {
		if (block instanceof MoistBlock && world instanceof IWorld) {
			return ((MoistBlock) block).getMoisture((IBlockState) world.getBlockState(pos), (IWorld) world, (IBlockPos) pos);
		}
		return g;
	}
}
