package io.github.iridis.internal.api.data;

import java.util.AbstractList;

import io.github.iridis.api.data.NBTList;

import net.minecraft.nbt.AbstractListTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class ListTagWrapper<T> extends AbstractList<T> implements NBTList<T> {
	final ListTag tags;

	public ListTagWrapper(ListTag tags) {this.tags = tags;}

	@SuppressWarnings ("unchecked")
	@Override
	public T get(int index) {
		return (T) TagUtil.toObject(this.tags.get(index));
	}

	@Override
	public T set(int index, T element) {
		return (T) TagUtil.toObject(this.tags.set(index, TagUtil.fromObject(this.tags.get(index))));
	}

	@Override
	public void add(int index, T element) {
		this.tags.add(index, TagUtil.fromObject(this.tags.get(index)));
	}

	@Override
	public T remove(int index) {
		return (T) TagUtil.toObject(this.tags.remove(index));
	}

	@Override
	public int size() {
		return this.tags.size();
	}

	@Override
	public ListTag get() {
		return this.tags;
	}
}
