package io.github.iridis.api.entity;

import java.util.UUID;

import io.github.iridis.api.context.ContextManager;
import v1_16_1.net.minecraft.entity.IEntity;
import v1_16_1.net.minecraft.server.IMinecraftServer;
import v1_16_1.net.minecraft.server.world.IServerWorld;

/**
 * this interface is a wrapper class for a UUID, or an instance of a currently living Entity
 */
public interface UUIDEntity {
	/**
	 * @return the uuid of the entity
	 */
	UUID getId();

	static UUIDEntity get(UUID uuid, boolean player) {
		if(player)
			return Player.get(uuid);
		IMinecraftServer server = ContextManager.getInstance()
		                                        .peekFirstOfType(IMinecraftServer.class);
		if (server == null) {
			return () -> uuid;
		}

		for (IServerWorld world : server.getWorlds()) {
			IEntity entity = world.getEntity(uuid);
			if (entity != null) {
				return (UUIDEntity) (Object) entity;
			}
		}

		return () -> uuid;
	}
}
