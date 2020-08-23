package io.github.iridis.internal.asm.mixin.api.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

import io.github.iridis.api.data.NBTag;
import io.github.iridis.api.data.Version;
import io.github.iridis.internal.api.data.TagUtil;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import v1_16_1.net.minecraft.nbt.ICompoundTag;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.PositionTracker;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagReader;

@Mixin (CompoundTag.class)
public abstract class CompoundTagMixin_NBTagImpl implements NBTag {
	@Shadow
	public abstract @Nullable Tag get(String key);

	@Shadow public @Nullable abstract Tag put(String key, Tag tag);

	@Shadow public abstract byte getType(String key);

	@Override
	public <T> @Nullable T get(@Nullable Version version, Type<T> type, String key) {
		Tag of = this.get(key);
		if (of != null && of.getType() == type.getNbtId()) {
			return (T) TagUtil.toObject(of);
		}
		return null;
	}

	@Override
	public <T> void put(@Nullable Version version, Type<T> type, String key, T value) {
		Tag tag = TagUtil.fromObject(value);
		if (tag.getType() != type.getNbtId()) {
			throw new IllegalArgumentException("TagUtil made tag" + tag + " but expected type " + type.getType());
		}
		this.put(key, tag);
	}

	@Override
	public ICompoundTag makeCompound(@Nullable Version version, String key) {
		CompoundTag tag = new CompoundTag();
		this.put(key, tag);
		return (ICompoundTag) tag;
	}

	@SuppressWarnings ("unchecked")
	@Override
	public List<ICompoundTag> makeList(@Nullable Version version, String key) {
		ListTag tags = new ListTag();
		this.put(key, tags);
		return (List<ICompoundTag>) (List) tags;
	}

	@Override
	public @Nullable <T> Type<T> getKey(@Nullable Version version, String key) {
		return Type.of(this.getType(key));
	}


}
