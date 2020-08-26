package io.github.iridis.api.util.java;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Logger;

import com.google.common.collect.Sets;
//import jdk.nashorn.internal.codegen.types.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * this is a map backed by 2 other maps, the first map is the map that stores `put` operations, and is primary in operations. the second map is assumed to
 * be immutable, even if it isn't.
 *
 * This is useful when there's some code your mixing into and people assume that hashmap isn't global, but you want to attach a global, by reference map to
 * it. Technically this isn't safe because the map may appear to mutate when no operations are done on it, but I cba fix that.
 *
 * @param <K>
 * @param <V>
 */
public class ReferenceDualMap<K, V> extends HashMap<K, V> implements Map<K, V> {
	static {
		Logger logger = Logger.getLogger(ReferenceDualMap.class.getSimpleName());
		logger.warning("--- Attention Furnace Tinkering Dev ---");
		logger.warning("ReferenceDual map is in use, a map may appear to mutate when no operations are done by it");
//		logger.warning("https://github.com/IridisMC/FebbAssembly/tree/master/src/main/java/" + Type.getInternalName(ReferenceDualMap.class) + ".java");
		logger.warning(
				"Iridis uses this to 'replace' the AbstractFurnaceBlockEntity#createFuelTimeMap map for compatibility. But it has the potential to open " +
				"create bugs");
	}

	private final Set<K> removed = Collections.newSetFromMap(new WeakHashMap<>());
	private final Map<K, V> a, b;

	/**
	 * @param a primary, preferred, put operations go there
	 * @param b assumed immutable
	 */
	public ReferenceDualMap(Map<K, V> a, Map<K, V> b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public int size() {
		return this.a.size() + this.b.size();
	}

	@Override
	public boolean isEmpty() {
		return this.a.isEmpty() && this.b.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return this.a.containsKey(key) || this.b.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return this.a.containsValue(value) || this.b.containsValue(value);
	}

	@Override
	public V get(Object key) {
		V obj = this.a.get(key);
		if (obj == null && !this.removed.contains(key)) {
			return this.b.get(key);
		}
		return obj;
	}

	@Nullable
	@Override
	public V put(K key, V value) {
		if (value == null) {
			this.removed.add(key);
		}
		return this.a.put(key, value);
	}

	@Override
	public V remove(Object key) {
		V val = this.a.remove(key);
		this.removed.add((K) key);
		if (val == null) {
			val = this.b.get(key);
		}
		return val;
	}

	@Override
	public void putAll(@NotNull Map<? extends K, ? extends V> m) {
		this.a.putAll(m);
	}

	@Override
	public void clear() {
		this.a.clear();
		this.b.forEach((k, v) -> this.removed.add(k));
	}

	@NotNull
	@Override
	public Set<K> keySet() {
		return Sets.union(this.a.keySet(), this.b.keySet());
	}

	@NotNull
	@Override
	public Collection<V> values() {
		throw new UnsupportedOperationException("values not supported!");
	}

	@NotNull
	@Override
	public Set<Entry<K, V>> entrySet() {
		return Sets.union(this.a.entrySet(), this.b.entrySet());
	}
}
