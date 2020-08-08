package io.github.iridis.internal.api.data;

import io.github.iridis.api.data.NBTag;

import net.minecraft.nbt.CompoundTag;

public interface InternalTag extends NBTag {
	CompoundTag getInternal();
}
