package io.github.iridis.api.data;

import java.util.HashMap;
import java.util.Map;

import io.github.iridis.api.context.ContextManager;
import net.devtech.nanoevents.util.Id;
import v1_16_1.net.minecraft.nbt.ICompoundTag;

/**
 * The API class for Serialization and Deserialization of objects via NBT, this is heavily WIP, but it works so I'm not complaining
 */
public class Data {
	private static final Map<Id, Readable> READERS = new HashMap<>();

	/**
	 * this should be implemented directly on the class you want to serialize, don't forget to register a deserializer
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
}
