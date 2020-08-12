package io.github.iridis.internal.api.data;

import java.util.UUID;

import io.github.iridis.api.data.NBTag;
import v1_16_1.net.minecraft.nbt.ICompoundTag;

public class NBTagUtil {
	public static ICompoundTag to(UUID uuid) {
		ICompoundTag tag = NBTag.create();
		tag.put(NBTag.Type.LONG, "uuid_least", uuid.getLeastSignificantBits());
		tag.put(NBTag.Type.LONG, "uuid_most", uuid.getMostSignificantBits());
		return tag;
	}

	public static UUID uuidFrom(ICompoundTag tag) {
		return new UUID(tag.get(NBTag.Type.LONG, "uuid_most"), tag.get(NBTag.Type.LONG, "uuid_least"));
	}
}
