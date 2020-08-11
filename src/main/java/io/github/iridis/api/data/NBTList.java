package io.github.iridis.api.data;

import net.minecraft.nbt.ListTag;

public interface NBTList<T> {
	/**
	 * @deprecated internal
	 */
	@Deprecated ListTag get();
}
