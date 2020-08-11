package io.github.iridis.internal.api.data;

import static net.fabricmc.fabric.api.util.NbtType.COMPOUND;

import java.util.logging.Logger;

import io.github.iridis.api.data.NBTList;

import net.minecraft.nbt.AbstractListTag;
import net.minecraft.nbt.AbstractNumberTag;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

/**
 * warning: code smell
 */
public class TagUtil {
	/**
	 * convert a tag to it's object
	 */
	public static Object toObject(Tag tag) {
		if (tag instanceof AbstractNumberTag) {
			return ((AbstractNumberTag) tag).getNumber();
		} else if (tag instanceof ListTag) {
			ListTag list = ((ListTag) tag);
			if (list.getElementType() == COMPOUND) {
				return list;
			} else {
				return new ListTagWrapper<>(list);
			}
		} else if(tag instanceof StringTag) {
			return tag.asString();
		} else if(tag instanceof IntArrayTag) {
			return ((IntArrayTag) tag).getIntArray();
		} else if(tag instanceof ByteArrayTag) {
			return ((ByteArrayTag) tag).getByteArray();
		} else if(tag instanceof LongArrayTag) {
			return ((LongArrayTag) tag).getLongArray();
		}

		return tag;
	}

	/**
	 * convert an object to it's tag
	 * @throws IllegalArgumentException if unrecognized type
	 */
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
		} else if(tag instanceof NBTList) {
			return ((NBTList) tag).get();
		} else if(tag instanceof String) {
			return StringTag.of((String) tag);
		} else if(tag instanceof int[]) {
			return new IntArrayTag((int[])tag);
		} else if(tag instanceof byte[]) {
			return new ByteArrayTag((byte[])tag);
		} else if(tag instanceof long[]) {
			return new LongArrayTag((long[])tag);
		}
		throw new IllegalArgumentException("invalid object type! " + tag);
	}
}
