package io.github.iridis.internal.events;

import io.github.iridis.internal.asm.mixin.events.block.BlockItemMixin_PostBlockPlaceEvent;
import io.github.iridis.internal.asm.mixin.events.block.BlockItemMixin_PreBlockPlaceEvent;
import io.github.iridis.internal.asm.mixin.events.block.FireBlockMixin_PostBlockBurnedEvent;
import io.github.iridis.internal.asm.mixin.events.block.FireBlockMixin_PreBlockBurnedEvent;
import io.github.iridis.internal.asm.mixin.events.block.ServerPlayerInteractionManagerMixin_PostBlockBreakEvent;
import io.github.iridis.internal.asm.mixin.events.block.ServerPlayerInteractionManagerMixin_PreBlockBreakEvent;
import io.github.iridis.internal.asm.mixin.events.block.WorldMixin_PreSetBlockEvent;
import io.github.iridis.internal.events.annotations.SingleMixin;
import net.devtech.nanoevents.api.Logic;
import net.devtech.nanoevents.api.annotations.Invoker;
import v1_16_1.net.minecraft.block.IBlockState;
import v1_16_1.net.minecraft.block.entity.IBlastFurnaceBlockEntity;
import v1_16_1.net.minecraft.block.entity.IBlockEntity;
import v1_16_1.net.minecraft.entity.player.IPlayerEntity;
import v1_16_1.net.minecraft.item.IItemPlacementContext;
import v1_16_1.net.minecraft.util.math.IBlockPos;
import v1_16_1.net.minecraft.world.IWorld;

@SuppressWarnings ("ReferenceToMixin")
public class BlockEvents {
	/**
	 * @return true if the event should be cancelled
	 */
	@Invoker (value = "iridis:pre_block_break", args = SingleMixin.class)
	@SingleMixin (ServerPlayerInteractionManagerMixin_PreBlockBreakEvent.class)
	public static boolean playerPreBreak(IPlayerEntity player, IWorld world, IBlockPos pos, IBlockState state, IBlockEntity entity) {
		Logic.start();
		if (playerPreBreak(player, world, pos, state, entity)) {
			return true;
		}
		Logic.end();
		return false;
	}

	/**
	 * post event of
	 *
	 * @see BlockEvents#playerPreBreak(IPlayerEntity, IWorld, IBlockPos, IBlockState, IBlockEntity)
	 */
	@Invoker (value = "iridis:post_block_break", args = SingleMixin.class)
	@SingleMixin (ServerPlayerInteractionManagerMixin_PostBlockBreakEvent.class)
	public static void playerPostBreak(IPlayerEntity player, IWorld world, IBlockPos pos, IBlockState state, IBlockEntity entity) {
		Logic.start();
		playerPostBreak(player, world, pos, state, entity);
		Logic.end();
	}

	/**
	 * called when a fire block burns through another block, removing it from the world
	 *
	 * @return true to cancel the event
	 */
	@Invoker (value = "iridis:pre_block_burned_event", args = SingleMixin.class)
	@SingleMixin (FireBlockMixin_PreBlockBurnedEvent.class)
	public static boolean preBlockBurnedEvent(IWorld world, IBlockPos pos) {
		Logic.start();
		if (preBlockBurnedEvent(world, pos)) {
			return true;
		}
		Logic.end();
		return false;
	}

	/**
	 * the post event for
	 *
	 * @see #preBlockBurnedEvent(IWorld, IBlockPos)
	 */
	@Invoker (value = "iridis:post_block_burned_event", args = SingleMixin.class)
	@SingleMixin (FireBlockMixin_PostBlockBurnedEvent.class)
	public static void postBlockBurnedEvent(IWorld world, IBlockPos pos) {
		Logic.start();
		postBlockBurnedEvent(world, pos);
		Logic.end();
	}

	/**
	 * called before a block is placed in the world by a player or other
	 *
	 * @return true to cancel the event
	 */
	@Invoker (value = "iridis:pre_block_place_event", args = SingleMixin.class)
	@SingleMixin (BlockItemMixin_PreBlockPlaceEvent.class)
	public static boolean preBlockPlaceEvent(IItemPlacementContext context) {
		Logic.start();
		if (preBlockPlaceEvent(context)) {
			return true;
		}
		Logic.end();
		return false;
	}

	/**
	 * post event for
	 *
	 * @see #preBlockPlaceEvent(IItemPlacementContext)
	 */
	@Invoker (value = "iridis:post_block_place_event", args = SingleMixin.class)
	@SingleMixin (BlockItemMixin_PostBlockPlaceEvent.class)
	public static void postBlockPlaceEvent(IItemPlacementContext context) {
		Logic.start();
		postBlockPlaceEvent(context);
		Logic.end();
	}

	/**
	 * called any time something calls World#setBlockState, this is one of the most called methods in the minecraft code, so make sure you keep track of
	 * how
	 * much time you're spending here!
	 *
	 * NanoEvents ensures this event has no overhead so it's something we can afford, but that's no excuse for you to slack on the performance front.
	 */
	@Invoker (value = "iridis:pre_block_set_event", args = SingleMixin.class)
	@SingleMixin (WorldMixin_PreSetBlockEvent.class)
	public static boolean preBlockSetEvent(IWorld world, IBlockPos pos, IBlockState newState) {
		Logic.start();
		if (preBlockSetEvent(world, pos, newState)) {
			return true;
		}
		Logic.end();
		return false;
	}

	/**
	 * post event for
	 *
	 * @see #preBlockSetEvent(IWorld, IBlockPos, IBlockState)
	 */
	@Invoker (value = "iridis:post_block_set_event", args = SingleMixin.class)
	@SingleMixin (BlockItemMixin_PostBlockPlaceEvent.class)
	public static void postBlockSetEvent(IWorld world, IBlockPos pos, IBlockState newState, IBlockState oldState) {
		Logic.start();
		postBlockSetEvent(world, pos, newState, oldState);
		Logic.end();
	}
}
