package io.github.iridis.api.block;

import v1_16_1.net.minecraft.block.IBlockState;
import v1_16_1.net.minecraft.util.math.IBlockPos;
import v1_16_1.net.minecraft.world.IWorld;

import net.minecraft.block.Block;

/**
 * hahahah funy word. Implemented on your Block class, determines the moisture of the block (a factor in crop growth speed)
 */
public interface MoistBlock {
	/**
	 * @return the moisture of your block, vanilla blocks have a moisture of 3
	 */
	float getMoisture(IBlockState state, IWorld world, IBlockPos pos);
}
