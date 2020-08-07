package io.github.iridis.internal.api.data;

import static net.fabricmc.fabric.api.util.NbtType.COMPOUND;

import net.minecraft.nbt.AbstractListTag;
import net.minecraft.nbt.AbstractNumberTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class TagUtil {
	public static Object toObject(Tag tag) {
		if (tag instanceof AbstractNumberTag) {
			return ((AbstractNumberTag) tag).getNumber();
		} else if (tag instanceof AbstractListTag) {
			ListTag list = ((ListTag) tag);
			if (list.getElementType() == COMPOUND) {
				return list;
			} else {
				return new ListTagWrapper<>(list);
			}
		}

		return tag;
	}

	public static Tag fromObject(Object tag) {
		// todo
	}
}
