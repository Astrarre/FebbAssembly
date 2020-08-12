package io.github.iridis.internal.merge;

import io.github.iridis.api.data.NBTag;
import v1_16_1.net.minecraft.item.IItemStack;

public interface IItemStackAttach {
	default int getCustomModelData() {
		return ((IItemStack)this).getTag().get(NBTag.Type.INT, "CustomModelData");
	}

	default void setCustomModelData(int modelData) {
		((IItemStack)this).getOrCreateTag().put(NBTag.Type.INT, "CustomModelData", modelData);
	}

	/**
	 * the tag lecterns and other blocks read from when they are placed to construct their block entity
	 */
	default NBTag getBlockEntityTag() {
		return ((IItemStack)this).getTag().get(NBTag.Type.COMPOUND, "BlockEntityTag");
	}
}
