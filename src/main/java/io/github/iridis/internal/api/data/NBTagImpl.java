package io.github.iridis.internal.api.data;

import io.github.iridis.api.data.NBTag;
import io.github.iridis.api.data.Version;
import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;

/**
 * todo implement
 */
public class NBTagImpl implements NBTag {
	private final CompoundTag tag;

	public NBTagImpl(CompoundTag tag) {
		this.tag = tag;
	}

	@Override
	public @Nullable <T> Type<T> getKey(@Nullable Version version, String key) {
		return null;
	}

	@Override
	public <T> @Nullable T get(@Nullable Version version, Type<T> type, String key) {
		return null;
	}
}
