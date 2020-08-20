package io.github.iridis.internal.api.data;

import io.github.iridis.api.context.ContextManager;
import io.github.iridis.api.data.Data;
import io.github.iridis.api.data.NBTag;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.devtech.nanoevents.util.Id;
import v1_16_1.net.minecraft.nbt.ICompoundTag;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class Serialization {
	/**
	 * @deprecated internal
	 */
	@Deprecated
	public static ListTag serialize(ObjectArrayList<Object> context) {
		ListTag tags = new ListTag();
		if (context != null) {
			for (Object o : context) {
				if (o instanceof Data.Writable) {
					ICompoundTag toWrite = ((Data.Writable) o).to();
					if (toWrite instanceof CompoundTag) {
						Id id = ((Data.Writable) o).id();
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
			Data.Readable readable = Data.get(id);
			Object obj = readable.from(ContextManager.getInstance(), (ICompoundTag) data);
			list.set(i, obj);
		}
		return list;
	}
}
