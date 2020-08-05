package io.github.iridis.internal.asm.mixin.events.block;

import java.util.Random;

import io.github.iridis.internal.events.BlockEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import v1_16_1.net.minecraft.util.math.IBlockPos;
import v1_16_1.net.minecraft.world.IWorld;

import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin (FireBlock.class)
public class FireBlockMixin_PreBlockBurnedEvent {
	@Inject (method = "trySpreadingFire",
			at = @At (value = "INVOKE",
					target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;",
					ordinal = 1),
			cancellable = true)
	private void event(World world, BlockPos pos, int spreadFactor, Random rand, int currentAge, CallbackInfo ci) {
		if (BlockEvents.preBlockBurnedEvent((IWorld) world, (IBlockPos) pos)) {
			ci.cancel();
		}
	}
}
