package io.github.iridis.internal.invokers;

import net.devtech.nanoevents.api.Logic;
import net.devtech.nanoevents.api.annotations.Invoker;
import v1_16_1.net.minecraft.block.IBlockState;
import v1_16_1.net.minecraft.block.entity.IBlockEntity;
import v1_16_1.net.minecraft.entity.player.IPlayerEntity;
import v1_16_1.net.minecraft.util.math.IBlockPos;
import v1_16_1.net.minecraft.world.IWorld;

public class BlockEvents {
	/**
	 * serverside
	 * @return true if the event should be cancelled
	 */
	@Invoker("iridis:pre_block_break")
	public static boolean playerPreBreak(IPlayerEntity player, IWorld world, IBlockPos pos, IBlockState state, IBlockEntity entity) {
		Logic.start();
		if(playerPreBreak(player, world, pos, state, entity)) {
			return true;
		}
		Logic.end();
		return false;
	}

	/**
	 * serverside
	 */
	@Invoker("iridis:post_block_break")
	public static void playerPostBreak(IPlayerEntity player, IWorld world, IBlockPos pos, IBlockState state, IBlockEntity entity) {
		Logic.start();
		playerPostBreak(player, world, pos, state, entity);
		Logic.end();
	}
}
