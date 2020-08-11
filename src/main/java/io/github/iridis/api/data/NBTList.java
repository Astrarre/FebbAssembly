package io.github.iridis.api.data;

import java.util.List;

import net.minecraft.nbt.ListTag;

public interface NBTList<T> extends List<T> {
	/**
	 * @deprecated internal
	 */
	@Deprecated ListTag get();
}
