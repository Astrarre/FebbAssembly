package io.github.iridis.internal.api.data;

import static net.fabricmc.fabric.api.util.NbtType.COMPOUND;

import net.minecraft.nbt.AbstractListTag;
import net.minecraft.nbt.AbstractNumberTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
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
		if(tag instanceof Byte) {
			return ByteTag.of((Byte) tag);
		} else if(tag instanceof Boolean) {
			return ByteTag.of((Boolean) tag);
		} else if(tag instanceof Short) {
			return ShortTag.of((Short) tag);
		} else if(tag instanceof Character) {
			return ShortTag.of((short) ((Character) tag).charValue());
		} else if(tag instanceof Integer) {
			return IntTag.of((Integer) tag);
		} else if(tag instanceof Float) {
			return FloatTag.of((Float) tag);
		} else if(tag instanceof Long) {
			return LongTag.of((Long) tag);
		} else if(tag instanceof Double) {
			return DoubleTag.of((Double) tag);
		} else if(tag instanceof InternalTag) {
			return ((InternalTag) tag).getInternal();
		}
		throw new IllegalArgumentException("invalid object type! " + tag);
	}
}
