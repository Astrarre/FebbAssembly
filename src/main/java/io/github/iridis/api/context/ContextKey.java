package io.github.iridis.api.context;

import java.util.Objects;

import io.github.iridis.api.data.NBTag;
import net.devtech.nanoevents.util.Id;

public class ContextKey<T> {
	protected final String key;
	protected final T object;

	private ContextKey(String key, T object) {
		this.key = key;
		this.object = object;
	}

	public static <T> ContextKey<T> of(String key, T object) {
		if(object instanceof ContextSerialization.Writable) {
			return new SerializableKey<>(key, object);
		}
		return object == null ? null : new ContextKey<>(key, object);
	}

	private static final class SerializableKey<T> extends ContextKey<T> implements ContextSerialization.Writable {
		private SerializableKey(String key, T object) {
			super(key, object);
		}

		@Override
		public NBTag to() {
			return ((ContextSerialization.Writable) this.object).to();
		}

		@Override
		public Id id() {
			return ((ContextSerialization.Writable) this.object).id();
		}
	}

	public String getKey() {
		return this.key;
	}

	public T getObject() {
		return this.object;
	}

	public Class<T> getType() {
		return this.object == null ? null : (Class<T>) this.object.getClass();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ContextKey)) {
			return false;
		}

		ContextKey key1 = (ContextKey) o;

		if (!Objects.equals(this.key, key1.key)) {
			return false;
		}
		return Objects.equals(this.object, key1.object);
	}

	@Override
	public int hashCode() {
		int result = this.key != null ? this.key.hashCode() : 0;
		result = 31 * result + (this.object != null ? this.object.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return '(' + this.key + ", " + this.object + ')';
	}
}
