package io.github.iridis.api.entity;

import java.util.UUID;

import com.mojang.authlib.GameProfile;
import io.github.iridis.api.context.DefaultContext;
import io.github.iridis.api.entity.player.OfflinePlayer;
import v1_16_1.net.minecraft.entity.player.IPlayerEntity;
import v1_16_1.net.minecraft.server.IMinecraftServer;
import v1_16_1.net.minecraft.server.network.IServerPlayerEntity;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.UserCache;

/**
 * implemented on {@link OfflinePlayer} and {@link v1_16_1.net.minecraft.entity.player.IPlayerEntity}
 */
public interface Player extends UUIDEntity {


	/**
	 * @return the name of the player
	 */
	String getName();

	@SuppressWarnings ("ConstantConditions")
	default boolean isOnline() {
		return this instanceof IPlayerEntity;
	}

	/**
	 * get the online or offline player for the uuid
	 */
	static Player get(UUID uuid) {
		IMinecraftServer server = DefaultContext.SERVER.get().peekFirstOfType(IMinecraftServer.class);
		if (server == null) {
			return new OfflinePlayer(new GameProfile(uuid, null));
		}

		IServerPlayerEntity entity = server.getPlayerManager()
		                                  .getPlayer(uuid);
		if (entity != null) {
			return entity;
		}
		UserCache cache = ((MinecraftServer)server).getUserCache();
		GameProfile profile = cache.getByUuid(uuid);
		if(profile == null) {
			profile =  new GameProfile(uuid, null);
		}
		return new OfflinePlayer(profile);
	}
}
