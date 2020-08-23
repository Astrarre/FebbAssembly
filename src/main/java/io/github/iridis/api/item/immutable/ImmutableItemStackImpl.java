package io.github.iridis.api.item.immutable;

import io.github.iridis.api.data.NBTag;
import io.github.iridis.api.item.immutable.ImmutableItemStack;
import v1_16_1.net.minecraft.item.IItem;
import v1_16_1.net.minecraft.item.IItemConvertible;
import v1_16_1.net.minecraft.item.IItemStack;
import v1_16_1.net.minecraft.nbt.ICompoundTag;

/**
 * an immutable version of {@link IItemStack}
 */
class ImmutableItemStackImpl implements ImmutableItemStack {
	private final int count;
	private final int cooldown;
	private final IItem item;
	private final ICompoundTag tag;

	public ImmutableItemStackImpl(IItemConvertible convertible) {
		this(convertible, null, 0, 0);
	}

	public ImmutableItemStackImpl(IItemConvertible convertible, ICompoundTag nbt) {
		this(convertible, nbt, 0 , 0);
	}

	public ImmutableItemStackImpl(IItemConvertible convertible, int count) {
		this(convertible, null, 0, count);
	}

	public ImmutableItemStackImpl(IItemConvertible item, ICompoundTag tag, int cooldown, int count) {
		this.count = count;
		this.cooldown = cooldown;
		this.item = item.asItem();
		this.tag = tag == null ? null : tag.copy();
	}

	@Override
	public int getCount() {
		return this.count;
	}

	@Override
	public int getCooldown() {
		return this.cooldown;
	}

	@Override
	public IItem getItem() {
		return this.item;
	}

	@Override
	public ICompoundTag getTag() {
		return NBTag.copy(this.tag);
	}

	@Override
	public IItemStack asItemStack() {
		IItemStack stack = IItemStack.create(this.item, this.count);
		stack.setCooldown(this.cooldown);
		stack.setTag(this.tag);
		return stack;
	}
}
