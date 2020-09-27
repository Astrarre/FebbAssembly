package io.github.iridis.internal.asm.mixin.api.context.entity;

import io.github.iridis.api.context.ContextManager;
import io.github.iridis.api.context.DefaultContext;
import io.github.iridis.internal.api.data.Serialization;
import io.github.iridis.internal.asm.access.ContextHolderAccess;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.util.NbtType;

@Mixin (Entity.class)
public abstract class EntityMixin implements ContextHolderAccess {
	@Shadow
	protected abstract void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition);

	@Shadow
	protected abstract void checkBlockCollision();

	@Shadow public World world;

	private ObjectArrayList<Object> creationContext;

	@Inject (method = "toTag", at = @At ("HEAD"))
	private void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
		tag.put("irids:creationContext", Serialization.serialize(this.creationContext));
	}

	@Inject (method = "fromTag", at = @At ("HEAD"))
	private void fromTag(CompoundTag tag, CallbackInfo ci) {
		ListTag tags = tag.getList("irids:creationContext", NbtType.COMPOUND);
		this.creationContext = Serialization.deserialize(tags);
	}

	@Override
	public ObjectArrayList<Object> getContext() {
		return this.creationContext;
	}

	@Override
	public void setContext(ObjectArrayList<Object> context) {
		this.creationContext = context;
	}

	@Redirect (method = "move",
			at = @At (value = "INVOKE",
					target = "Lnet/minecraft/entity/Entity;fall(DZLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V"))
	private void move(Entity entity, double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition, MovementType type,
	                  Vec3d movement) {
		ContextManager manager = DefaultContext.BLAME.get();
		manager.push("entityFall", this);
		manager.actStack(((ContextHolderAccess) this).getContext(), () -> {
			this.fall(heightDifference, onGround, landedState, landedPosition);
			return null;
		});
		manager.pop("entityFall", this);
	}

	@Redirect (method = "move",
			at = @At (value = "INVOKE", target = "Lnet/minecraft/block/Block;onEntityLand(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;)V"))
	private void move(Block block, BlockView world, Entity entity, MovementType type, Vec3d movement) {
		ContextManager manager = DefaultContext.BLAME.get();
		manager.push("entityLand", this);
		manager.actStack(((ContextHolderAccess) this).getContext(), () -> {
			block.onEntityLand(world, entity);
			return null;
		});
		manager.pop("entityLand", this);
	}

	@Redirect (method = "move",
			at = @At (value = "INVOKE",
					target = "Lnet/minecraft/block/Block;onSteppedOn(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;" + "Lnet/minecraft" +
					         "/entity/Entity;)V"))
	private void move(Block block, World world, BlockPos pos, Entity entity, MovementType type, Vec3d movement) {
		ContextManager manager = DefaultContext.BLAME.get();
		manager.push("entityStep", this);
		manager.actStack(((ContextHolderAccess) this).getContext(), () -> {
			block.onSteppedOn(world, pos, entity);
			return null;
		});
		manager.pop("entityStep", this);
	}

	@Redirect (method = "move", at = @At (value = "INVOKE", target = "Lnet/minecraft/entity/Entity;checkBlockCollision()V"))
	private void move(Entity entity, MovementType type, Vec3d movement) {
		ContextManager manager = DefaultContext.BLAME.get();
		manager.push("entityCollide", this);
		manager.actStack(((ContextHolderAccess) this).getContext(), () -> {
			this.checkBlockCollision();
			return null;
		});
		manager.pop("entityCollide", this);
	}
}
