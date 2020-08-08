package io.github.iridis.api.context;

import java.util.HashMap;
import java.util.Map;

import io.github.iridis.api.data.NBTag;
import net.devtech.nanoevents.util.Id;

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
		NBTag to();

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
		Object from(ContextManager manager, NBTag data);
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
