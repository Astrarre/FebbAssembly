package io.github.iridis.api.player;

import java.util.UUID;

import io.github.iridis.api.data.NBTag;

public class NBTagUtil {
	public static NBTag to(UUID uuid) {
		NBTag tag = NBTag.create();
		tag.put(NBTag.Type.LONG, "uuid_least", uuid.getLeastSignificantBits());
		tag.put(NBTag.Type.LONG, "uuid_most", uuid.getMostSignificantBits());
		return tag;
	}

	public static UUID uuidFrom(NBTag tag) {
		return new UUID(tag.get(NBTag.Type.LONG, "uuid_most"), tag.get(NBTag.Type.LONG, "uuid_least"));
	}
}
