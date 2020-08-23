package io.github.iridis.api.item.immutable;

import org.jetbrains.annotations.Nullable;
import v1_16_1.net.minecraft.item.IItem;
import v1_16_1.net.minecraft.item.IItemConvertible;
import v1_16_1.net.minecraft.item.IItemStack;
import v1_16_1.net.minecraft.nbt.ICompoundTag;

import net.minecraft.item.ItemStack;

public interface ImmutableItemStack {
	static ImmutableItemStack create(IItemConvertible convertible) {
		return new ImmutableItemStackImpl(convertible);
	}

	static ImmutableItemStack create(IItemConvertible convertible, ICompoundTag nbt) {
		return new ImmutableItemStackImpl(convertible, nbt);
	}

	static ImmutableItemStack create(IItemConvertible convertible, int count) {
		return new ImmutableItemStackImpl(convertible, count);
	}

	static ImmutableItemStack create(IItemConvertible item, ICompoundTag tag, int cooldown, int count) {
		return new ImmutableItemStackImpl(item, tag, cooldown, count);
	}

	static ImmutableItemStack create(IItemStack stack) {
		return new ImmutableItemStackWrapper(stack.copy());
	}


	int getCount();

	int getCooldown();

	IItem getItem();

	/**
	 * @return a copy of the nbt data of the item
	 */
	@Nullable
	ICompoundTag getTag();

	IItemStack asItemStack();
}
