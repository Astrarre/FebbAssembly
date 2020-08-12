package io.github.iridis.api.context;

import java.util.HashMap;
import java.util.Map;

import io.github.iridis.api.data.NBTag;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.devtech.nanoevents.util.Id;
import v1_16_1.net.minecraft.nbt.ICompoundTag;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

/**
 * ContextSerializable objects can be stored persistently across resets, making them useful for Scheduled Ticks and keeping track of who placed a
 * BlockEntity
 */
public class ContextSerialization {
	private static final Map<Id, Readable> READERS = new HashMap<>();

	/**
	 * this should be implemented directly on the class you want to serialize
	 */
	public interface Writable {
		/**
		 * serialize the object
		 */
		ICompoundTag to();

		/**
		 * @return the id of the reader
		 */
		Id id();
	}

	/**
	 * this should be registered with the same id as the writer
	 */
	public interface Readable {
		/**
		 * the return type of this function doesn't need to be the same as the original type, for example, authentic PlayerEntities can't always be
		 * guaranteed to exist at read time, for example if a player leaves some data in the ContextManager, then leaves the game, the reader has the
		 * freedom to deserialize it as an OfflinePlayer, instead of a PlayerEntity
		 */
		Object from(ContextManager manager, ICompoundTag data);
	}

	public static void register(Id id, Readable readable) {
		if (READERS.put(id, readable) != null) {
			throw new IllegalStateException("reader for id " + id + " already registered!");
		}
	}

	public static Readable get(Id id) {
		return READERS.get(id);
	}

	/**
	 * @deprecated internal
	 */
	@Deprecated
	public static ListTag serialize(ObjectArrayList<Object> context) {
		ListTag tags = new ListTag();
		if (context != null) {
			for (Object o : context) {
				if (o instanceof ContextSerialization.Writable) {
					ICompoundTag toWrite = ((ContextSerialization.Writable) o).to();
					if (toWrite instanceof CompoundTag) {
						Id id = ((ContextSerialization.Writable) o).id();
						toWrite.put(NBTag.Type.STRING, "ser_modid", id.mod);
						toWrite.put(NBTag.Type.STRING, "ser_value", id.value);
						tags.add(((CompoundTag) toWrite));
					} else {
						throw new IllegalArgumentException(o + "'s ContextSerialization.Writable#to returned a non-internaltag NBTag instance!");
					}
				}
			}
		}
		return tags;
	}

	/**
	 * @deprecated internal
	 */
	@Deprecated
	public static ObjectArrayList<Object> deserialize(ListTag tags) {
		ObjectArrayList<Object> list = new ObjectArrayList<>();
		for (int i = 0; i < tags.size(); i++) {
			CompoundTag data = tags.getCompound(i);
			Id id = new Id(data.getString("ser_modid"), data.getString("ser_value"));
			ContextSerialization.Readable readable = ContextSerialization.get(id);
			Object obj = readable.from(ContextManager.getInstance(), (ICompoundTag) data);
			list.set(i, obj);
		}
		return list;
	}
}
