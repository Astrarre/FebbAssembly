package io.github.iridis.api.item.immutable;

import v1_16_1.net.minecraft.item.IItem;
import v1_16_1.net.minecraft.item.IItemStack;
import v1_16_1.net.minecraft.nbt.ICompoundTag;

class ImmutableItemStackWrapper implements ImmutableItemStack {
	private final IItemStack stack;

	ImmutableItemStackWrapper(IItemStack stack) {this.stack = stack;}

	@Override
	public int getCount() {
		return this.stack.getCount();
	}

	@Override
	public int getCooldown() {
		return this.stack.getCooldown();
	}

	@Override
	public IItem getItem() {
		return this.stack.getItem();
	}

	@Override
	public ICompoundTag getTag() {
		return this.stack.getTag() != null ? this.stack.getTag().copy() : null;
	}

	@Override
	public IItemStack asItemStack() {
		return this.stack.copy();
	}
}
