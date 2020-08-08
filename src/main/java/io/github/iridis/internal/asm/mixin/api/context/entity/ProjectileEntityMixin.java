package io.github.iridis.internal.asm.mixin.api.context.entity;

import java.util.UUID;

import io.github.iridis.api.entity.UUIDEntity;
import io.github.iridis.internal.asm.access.ContextHolderAccess;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.CompoundTag;

@Mixin (ProjectileEntity.class)
public abstract class ProjectileEntityMixin extends EntityMixin {
	private boolean player;

	@Shadow private UUID ownerUuid;

	@Inject (method = "setOwner", at = @At (value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getUuid()Ljava/util/UUID;"))
	private void trackPlayer(@Nullable Entity entity, CallbackInfo ci) {
		this.player = entity instanceof PlayerEntity;
	}

	@Inject (method = "writeCustomDataToTag", at = @At ("HEAD"))
	public void trackPlayerWrite(CompoundTag tag, CallbackInfo ci) {
		tag.putBoolean("ownerIsPlayer", this.player);
	}

	@Inject (method = "readCustomDataFromTag", at = @At ("HEAD"))
	public void trackPlayerRead(CompoundTag tag, CallbackInfo ci) {
		this.player = tag.getBoolean("ownerIsPlayer");
	}

	/**@Override
	public ObjectArrayList<Object> getContext() {
		ObjectArrayList<Object> context = super.getContext();
		ObjectArrayList<Object> newContext = context == null ? new ObjectArrayList<>() : context.clone();
		newContext.push(UUIDEntity.get(this.ownerUuid, this.player));
		return newContext;
	}*/
}
