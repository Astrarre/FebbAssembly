package io.github.iridis.internal.asm.mixin.api.data;

import io.github.iridis.api.data.NBTag;
import io.github.iridis.api.data.Version;
import io.github.iridis.internal.api.data.InternalTag;
import io.github.iridis.internal.api.data.TagUtil;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

@Mixin (CompoundTag.class)
public abstract class CompoundTagMixin_NBTag implements InternalTag {
	@Shadow
	public abstract @Nullable Tag get(String key);

	@Override
	public @Nullable <T> Type<T> getKey(@Nullable Version version, String key) {
		return Type.of(this.getType(key));
	}

	@Shadow
	public abstract byte getType(String key);

	@Shadow public @Nullable abstract Tag put(String key, Tag tag);

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
		if(tag.getType() != type.getNbtId()) {
			throw new IllegalArgumentException("TagUtil made tag" + tag + " but expected type " + type.getType());
		}
		this.put(key, tag);
	}

	@Override
	public CompoundTag getInternal() {
		return (CompoundTag) (Object) this;
	}
}
