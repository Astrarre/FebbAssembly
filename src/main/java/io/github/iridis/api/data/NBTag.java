package io.github.iridis.api.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.util.collection.Int2ObjectBiMap;

import net.fabricmc.fabric.api.util.NbtType;

/**
 * @implNote create a wrapper class for CompoundTag when a key name changes
 */
public interface NBTag {
	/**
	 * create a new nbt tag for a minecraft version, this is used for nested values
	 *
	 * @return the newly created tag, or the current tag for that key
	 */
	NBTag makeCompound(@Nullable Version version, String key);

	/**
	 * create a new list tag for a minecraft version, this is used for nested values
	 *
	 * @return the newly created tag, or the current tag for that key
	 */
	List<NBTag> makeList(@Nullable Version version, String key);

	/**
	 * get the type in the given key
	 */
	@Nullable
	default <T> Type<T> getKey(String key) {
		return this.getKey(null, key);
	}

	/**
	 * get the type of data in the given key
	 *
	 * @return boolean and character types are both never returned
	 * @apiNote boolean and character both share types with byte and short respectively
	 */
	@Nullable <T> Type<T> getKey(@Nullable Version version, String key);

	/**
	 * @param key the key in which the type
	 * @throws ClassCastException if the key is not of type T
	 */
	@Nullable
	default <T> T get(Type<T> type, String key) {
		return this.get(null, type, key);
	}

	/**
	 * get a key from the object for a given version, so if mojang changes the key in an nbt object, your code wont break
	 *
	 * @param type the expected type
	 * @param key the key in the object
	 * @param version the minecraft version
	 */
	@Nullable <T> T get(@Nullable Version version, Type<T> type, String key);

	/**
	 * @param key the key in which the type
	 * @throws ClassCastException if the key is not of type T
	 */
	default <T> void put(Type<T> type, String key, T value) {
		this.put(null, type, key, value);
	}

	/**
	 * put a value into the object for a given version, so if mojang changes the key in an nbt object, your code wont break
	 *
	 * @param type the expected type
	 * @param key the key in the object
	 * @param version the minecraft version
	 */
	<T> void put(@Nullable Version version, Type<T> type, String key, T value);

	final class Type<T> {
		// keep these first so they're overriden
		public static final Type<Boolean> BOOLEAN = new Type<>(Boolean.class, NbtType.BYTE);
		public static final Type<Character> CHAR = new Type<>(Character.class, NbtType.SHORT);

		public static final Type<Byte> BYTE = new Type<>(Byte.class, NbtType.BYTE);
		public static final Type<Short> SHORT = new Type<>(Short.class, NbtType.SHORT);
		public static final Type<Integer> INT = new Type<>(Integer.class, NbtType.INT);
		public static final Type<Long> LONG = new Type<>(Long.class, NbtType.LONG);
		public static final Type<Float> FLOAT = new Type<>(Float.class, NbtType.FLOAT);
		public static final Type<Double> DOUBLE = new Type<>(Double.class, NbtType.DOUBLE);
		public static final Type<byte[]> BYTE_ARRAY = new Type<>(byte[].class, NbtType.BYTE_ARRAY);
		public static final Type<String> STRING = new Type<>(String.class, NbtType.STRING);
		public static final Type<NBTag> COMPOUND = new Type<>(NBTag.class, NbtType.COMPOUND);
		public static final Type<int[]> INT_ARRAY = new Type<>(int[].class, NbtType.INT_ARRAY);
		public static final Type<long[]> LONG_ARRAY = new Type<>(long[].class, NbtType.LONG_ARRAY);
		private static final Map<Class<?>, Type<?>> CLASS_TYPE_MAP = new HashMap<>();
		/**
		 * @deprecated internal
		 */
		@SuppressWarnings ("DeprecatedIsStillUsed") @Deprecated private static final Int2ObjectBiMap<Type<?>> NBT_ID_TYPE_MAP = new Int2ObjectBiMap<>(256);

		private final Class<T> type;
		private final int nbtId;
		private Type<List<T>> listType;

		private Type(Class<T> type, int id) {
			this.type = type;
			this.nbtId = id;
			if (type != List.class) {
				NBT_ID_TYPE_MAP.put(this, id);
				CLASS_TYPE_MAP.put(type, this);
			}
		}

		public Class<T> getType() {
			return this.type;
		}

		@NotNull
		@SuppressWarnings ("unchecked")
		public static <T> Type<T> of(Class<T> type) {
			return (Type<T>) CLASS_TYPE_MAP.getOrDefault(type, COMPOUND);
		}

		/**
		 * @deprecated internal
		 */
		@Deprecated
		@SuppressWarnings ("unchecked")
		public static <T> Type<T> of(int id) {
			return (Type<T>) NBT_ID_TYPE_MAP.get(id);
		}

		/**
		 * @deprecated internal
		 */
		@Deprecated
		public static int getId(Type<?> type) {
			return NBT_ID_TYPE_MAP.getId(type);
		}

		/**
		 * get the list type for this type
		 */
		@SuppressWarnings ({
				"unchecked",
				"rawtypes"
		})
		public Type<List<T>> getListType() {
			if (this.listType == null) {
				return this.listType = new Type<List<T>>((Class) List.class, NbtType.LIST);
			}
			return this.listType;
		}

		/**
		 * @deprecated internal
		 */
		@Deprecated
		public int getNbtId() {
			return this.nbtId;
		}
	}
}
