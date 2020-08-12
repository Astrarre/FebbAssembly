package io.github.iridis.internal.asm.mixin.api.context.blockentity;

import io.github.iridis.api.context.ContextSerialization;
import io.github.iridis.internal.asm.access.ContextHolderAccess;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import net.fabricmc.fabric.api.util.NbtType;

@Mixin (BlockEntity.class)
public class BlockEntityMixin implements ContextHolderAccess {
	private ObjectArrayList<Object> placementContext = new ObjectArrayList<>();

	@Override
	public void setContext(ObjectArrayList<Object> context) {
		this.placementContext = context;
	}

	@Override
	public ObjectArrayList<Object> getContext() {
		return this.placementContext;
	}

	@Inject (method = "toTag", at = @At ("HEAD"))
	private void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
		tag.put("irids:context_", ContextSerialization.serialize(this.placementContext));
	}

	@Inject (method = "fromTag", at = @At ("HEAD"))
	private void fromTag(BlockState state, CompoundTag tag, CallbackInfo ci) {
		ListTag tags = tag.getList("irids:context_", NbtType.COMPOUND);
		this.placementContext = ContextSerialization.deserialize(tags);
	}
}
