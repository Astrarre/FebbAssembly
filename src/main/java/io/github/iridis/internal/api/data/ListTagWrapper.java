package io.github.iridis.internal.api.data;

import java.util.AbstractList;

import net.minecraft.nbt.AbstractListTag;
import net.minecraft.nbt.Tag;

public class ListTagWrapper<T> extends AbstractList<T> {
	private final AbstractListTag<Tag> tags;

	public ListTagWrapper(AbstractListTag<Tag> tags) {this.tags = tags;}

	@SuppressWarnings ("unchecked")
	@Override
	public T get(int index) {
		return (T) TagUtil.toObject(this.tags.get(index));
	}

	@Override
	public T set(int index, T element) {
		return this.tags.set(index, );
	}

	@Override
	public void add(int index, T element) {
		super.add(index, element);
	}

	@Override
	public T remove(int index) {
		return super.remove(index);
	}

	@Override
	public int size() {
		return this.tags.size();
	}
}
