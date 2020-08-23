package io.github.iridis.api.entity.player;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.mojang.authlib.GameProfile;
import io.github.iridis.api.context.ContextManager;
import io.github.iridis.api.data.Data;
import io.github.iridis.api.entity.Player;
import io.github.iridis.api.util.TriId;
import io.github.iridis.internal.asm.mixin.access.PlayerManagerAccess;
import net.devtech.nanoevents.util.Id;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import v1_16_1.net.minecraft.nbt.ICompoundTag;
import v1_16_1.net.minecraft.server.IMinecraftServer;
import v1_16_1.net.minecraft.server.IPlayerManager;
import v1_16_1.net.minecraft.server.network.IServerPlayerEntity;

import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.Tag;
import net.minecraft.server.network.ServerPlayerEntity;

public class OfflinePlayer implements Data.Writable, Player {
	private static final Id OFFLINE_PLAYER_ID = new Id("iridis", "offline_player");

	static {
		Data.register(OFFLINE_PLAYER_ID, (c, t) -> {
			UUID uuid = NbtHelper.toUuid((Tag) (Object) t);
			return Player.get(uuid);
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
	public OfflinePlayer(GameProfile profile) {
		if (profile.getId() != null) {
			this.profile = profile;
		} else {
			throw new IllegalArgumentException("profile must contain uuid!");
		}
	}

	@Override
	@NotNull
	public UUID getId() {
		return this.profile.getId();
	}

	@Override
	@Nullable
	public String getName() {
		return this.profile.getName();
	}

	/**
	 * modify the inventory and general data of the server player entity, and then save immediately if the player is offline
	 */
	public void modifyOffline(Consumer<IServerPlayerEntity> consumer) {
		IMinecraftServer server = ContextManager.getInstance().peekFirstOfTypeOrThrow(IMinecraftServer.class);
		IPlayerManager manager = server.getPlayerManager();
		IServerPlayerEntity entity = manager.getPlayer(this.getId());
		if(entity == null) {
			IServerPlayerEntity tempPlayer = manager.createPlayer(this.profile);
			consumer.accept(tempPlayer);
			((PlayerManagerAccess) (Object) manager).callSavePlayerData((ServerPlayerEntity) tempPlayer);
		} else {
			consumer.accept(entity);
		}
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
	public ICompoundTag to() {
		return (ICompoundTag) NbtHelper.fromUuid(this.getId());
	}

	@Override
	public Id id() {
		return OFFLINE_PLAYER_ID;
	}
}
