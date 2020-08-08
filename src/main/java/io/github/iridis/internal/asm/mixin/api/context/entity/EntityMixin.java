package io.github.iridis.internal.asm.mixin.api.context.entity;

import io.github.iridis.api.context.ContextManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

@Mixin (Entity.class)
public abstract class EntityMixin {
	@Shadow
	protected abstract void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition);

	@Shadow
	protected abstract void checkBlockCollision();

	@Shadow public World world;

	@Redirect (method = "move",
			at = @At (value = "INVOKE",
					target = "Lnet/minecraft/entity/Entity;fall(DZLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V"))
	private void move(Entity entity, double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition, MovementType type,
	                  Vec3d movement) {
		ContextManager.getInstance()
		              .push(this);
		this.fall(heightDifference, onGround, landedState, landedPosition);
		ContextManager.getInstance()
		              .pop(this);
	}

	@Redirect (method = "move",
			at = @At (value = "INVOKE", target = "Lnet/minecraft/block/Block;onEntityLand(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;)V"))
	private void move(Block block, BlockView world, Entity entity, MovementType type, Vec3d movement) {
		ContextManager.getInstance()
		              .push(this);
		block.onEntityLand(world, entity);
		ContextManager.getInstance()
		              .pop(this);
	}

	@Redirect (method = "move",
			at = @At (value = "INVOKE",
					target = "Lnet/minecraft/block/Block;onSteppedOn(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;" + "Lnet/minecraft" +
					         "/entity/Entity;)V"))
	private void move(Block block, World world, BlockPos pos, Entity entity, MovementType type, Vec3d movement) {
		ContextManager.getInstance()
		              .push(this);
		block.onSteppedOn(world, pos, entity);
		ContextManager.getInstance()
		              .pop(this);
	}

	@Redirect (method = "move", at = @At (value = "INVOKE", target = "Lnet/minecraft/entity/Entity;checkBlockCollision()V"))
	private void move(Entity entity, MovementType type, Vec3d movement) {
		ContextManager.getInstance()
		              .push(this);
		this.checkBlockCollision();
		ContextManager.getInstance()
		              .pop(this);
	}
}
