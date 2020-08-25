package io.github.iridis.internal.asm.mixin.api.item;

import io.github.iridis.internal.merge.IItemSettingsAttach;
import org.spongepowered.asm.mixin.Mixin;
import v1_16_1.net.minecraft.item.IItem;

import net.minecraft.item.Item;

@Mixin(Item.Settings.class)
public class ItemSettingsMixin_AttachImpl implements IItemSettingsAttach
{
	private int iridis_burnTime;

	@Override
	public IItem.Settings burnTime(int time) {
		this.iridis_burnTime = time;
		return (IItem.Settings) this;
	}

	@Override
	public int burnTime() {
		return this.iridis_burnTime;
	}
}
