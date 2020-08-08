package io.github.iridis.api.player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.mojang.authlib.GameProfile;
import io.github.iridis.api.context.ContextSerialization;
import io.github.iridis.api.data.NBTag;
import io.github.iridis.api.util.TriId;
import net.devtech.nanoevents.util.Id;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.UserCache;

public class OfflinePlayer implements ContextSerialization.Writable {
	private static final Id OFFLINE_PLAYER_ID = new Id("iridis", "offline_player");

	static {
		ContextSerialization.register(OFFLINE_PLAYER_ID, (c, t) -> {
			MinecraftServer server = c.peekFirstOfType(MinecraftServer.class);
			UUID uuid = NBTagUtil.uuidFrom(t);
			if (server == null) {
				return new OfflinePlayer(new GameProfile(uuid, null));
			}
			ServerPlayerEntity entity = server.getPlayerManager()
			                                  .getPlayer(uuid);
			if (entity != null) {
				return entity;
			}
			UserCache cache = server.getUserCache();
			GameProfile profile = cache.getByUuid(uuid);
			return new OfflinePlayer(profile);
		});
	}

	/**
	 * @deprecated internal
	 */
	@Deprecated private final GameProfile profile;

	/**
	 * @deprecated internal
	 */
	@Deprecated
	public OfflinePlayer(Object profile) {
		if (profile instanceof GameProfile && ((GameProfile) profile).getId() != null) {
			this.profile = (GameProfile) profile;
		} else {
			throw new IllegalArgumentException("profile must be instance of " + GameProfile.class);
		}
	}

	@NotNull
	public UUID getId() {
		return this.profile.getId();
	}

	@Nullable
	public String getName() {
		return this.profile.getName();
	}

	public List<TriId> getProperty(String name) {
		return this.profile.getProperties()
		                   .get(name)
		                   .stream()
		                   .map(TriId::new)
		                   .collect(Collectors.toList());
	}

	public void putProperty(String name, TriId id) {
		this.profile.getProperties()
		            .get(name)
		            .add(id.toProperty());
	}

	public boolean isLegacy() {
		return this.profile.isLegacy();
	}

	@Override
	public NBTag to() {
		return NBTagUtil.to(this.getId());
	}

	@Override
	public Id id() {
		return OFFLINE_PLAYER_ID;
	}
}
