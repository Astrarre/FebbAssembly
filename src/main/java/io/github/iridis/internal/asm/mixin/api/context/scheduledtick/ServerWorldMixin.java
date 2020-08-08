package io.github.iridis.internal.asm.mixin.api.context.scheduledtick;

import java.util.Random;

import io.github.iridis.api.context.ContextManager;
import io.github.iridis.internal.asm.access.ContextHolderAccess;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.World;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
	@Redirect (method = "tickBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;scheduledTick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)V"))
	private void scheduled(BlockState state, ServerWorld world, BlockPos pos, Random random, ScheduledTick<Block> tick) {
		ObjectArrayList<Object> objects = ((ContextHolderAccess)tick).getContext(null);
		ContextManager.getInstance()
		              .pushStack(objects, () -> {
			state.scheduledTick(world, pos, random);
			return null;
		});
	}

	@Redirect (method = "tickFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;onScheduledTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"))
	private void scheduled(FluidState state, World world, BlockPos pos, ScheduledTick<Fluid> tick) {
		ObjectArrayList<Object> objects = ((ContextHolderAccess)tick).getContext(null);
		ContextManager.getInstance()
		              .pushStack(objects, () -> {
			              state.onScheduledTick(world, pos);
			              return null;
		              });
	}
}
