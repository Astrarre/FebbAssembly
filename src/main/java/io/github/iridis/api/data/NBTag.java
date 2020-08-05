package io.github.iridis.api.data;

import java.util.List;

import org.jetbrains.annotations.Nullable;

public interface NBTag {
	/**
	 * get the type in the given key
	 */
	@Nullable
	default <T> Type<T> getKey(String key) {
		return this.getKey(null, key);
	}

	/**
	 * get the type of data in the given key
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

	final class Type<T> {
		public static final Type<Byte> BYTE = new Type<>(Byte.class);
		public static final Type<Short> SHORT = new Type<>(Short.class);
		public static final Type<Integer> INT = new Type<>(Integer.class);
		public static final Type<Long> LONG = new Type<>(Long.class);
		public static final Type<Boolean> BOOLEAN = new Type<>(Boolean.class);
		public static final Type<Character> CHAR = new Type<>(Character.class);
		public static final Type<Float> FLOAT = new Type<>(Float.class);
		public static final Type<Double> DOUBLE = new Type<>(Double.class);
		public static final Type<byte[]> BYTE_ARRAY = new Type<>(byte[].class);
		public static final Type<String> STRING = new Type<>(String.class);
		public static final Type<NBTag> COMPOUND = new Type<>(NBTag.class);
		public static final Type<int[]> INT_ARRAY = new Type<>(int[].class);
		public static final Type<long[]> LONG_ARRAY = new Type<>(long[].class);

		private final Class<T> type;
		private Type<List<T>> listType;

		private Type(Class<T> type) {
			this.type = type;
		}

		public Class<T> getType() {
			return this.type;
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
				return this.listType = new Type<List<T>>((Class) List.class);
			}
			return this.listType;
		}
	}
}
