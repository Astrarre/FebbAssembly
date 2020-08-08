package io.github.iridis.internal.asm.mixin.api.context.blockentity;

import java.util.HashMap;
import java.util.Map;

import io.github.iridis.api.context.ContextManager;
import io.github.iridis.api.context.ContextSerialization;
import io.github.iridis.api.data.NBTag;
import io.github.iridis.internal.api.data.InternalTag;
import io.github.iridis.internal.asm.access.ContextHolderAccess;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.devtech.nanoevents.util.Id;
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
	private Map<String, ObjectArrayList<Object>> contextMap = new HashMap<>();

	@Override
	public void setContext(String reason, ObjectArrayList<Object> context) {
		this.contextMap.put(reason, context);
	}

	@Override
	public ObjectArrayList<Object> getContext(String reason) {
		return this.contextMap.get(reason);
	}

	@Inject (method = "toTag", at = @At ("HEAD"))
	private void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
		CompoundTag context = new CompoundTag();
		this.contextMap.forEach((k, v) -> {
			ListTag tags = new ListTag();
			for (Object o : v) {
				if (o instanceof ContextSerialization.Writable) {
					NBTag toWrite = ((ContextSerialization.Writable) o).to();
					if (toWrite instanceof InternalTag) {
						Id id = ((ContextSerialization.Writable) o).id();
						toWrite.put(NBTag.Type.STRING, "ser_modid", id.mod);
						toWrite.put(NBTag.Type.STRING, "ser_value", id.value);
						tags.add(((InternalTag) toWrite).getInternal());
					} else {
						throw new IllegalArgumentException(o + "'s ContextSerialization.Writable#to returned a non-internaltag NBTag instance!");
					}
				}
			}
			context.put(k, tags);
		});
		tag.put("irids:context_", context);
	}

	@Inject(method = "fromTag", at = @At("HEAD"))
	private void fromTag(BlockState state, CompoundTag tag, CallbackInfo ci) {
		CompoundTag context = tag.getCompound("context");
		for (String key : context.getKeys()) {
			ListTag tags = context.getList(key, NbtType.COMPOUND);
			ObjectArrayList<Object> list = new ObjectArrayList<>();
			for (int i = 0; i < tags.size(); i++) {
				CompoundTag data = tags.getCompound(i);
				Id id = new Id(data.getString("ser_modid"), data.getString("ser_value"));
				ContextSerialization.Readable readable = ContextSerialization.get(id);
				Object obj = readable.from(ContextManager.getInstance(), (NBTag) data);
				list.set(i, obj);
			}
			this.contextMap.put(key, list);
		}
	}
}
