package io.github.iridis.internal.merge;

import v1_16_1.net.minecraft.item.IItem;

public interface IItemSettingsAttach {
	/**
	 * set the burn time of the item in ticks
	 */
	IItem.Settings burnTime(int ticks);

	int burnTime();
}
