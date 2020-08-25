package io.github.iridis.internal.api.item;

import java.util.HashMap;
import java.util.Map;

import io.github.iridis.api.util.java.ReferenceDualMap;

import net.minecraft.item.Item;

public class BurnTimeRegistry {
	private static final Map<Item, Integer> REGISTRY = new HashMap<>();
	public static void put(Item item, int burnTime) {
		REGISTRY.put(item, burnTime);
	}

	public static Map<Item, Integer> wrap(Map<Item, Integer> map) {
		return new ReferenceDualMap<>(map, REGISTRY);
	}
}
