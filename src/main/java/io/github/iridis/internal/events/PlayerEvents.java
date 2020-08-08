package io.github.iridis.internal.events;

import io.github.iridis.api.entity.player.OfflinePlayer;
import io.github.iridis.internal.asm.mixin.events.player.PlayerManagerMixin_PostPlayerJoinEvent;
import io.github.iridis.internal.asm.mixin.events.player.PlayerManagerMixin_PrePlayerJoinEvent;
import io.github.iridis.internal.asm.mixin.events.player.ServerPlayerEntityMixin_PlayerSpawnEvent;
import io.github.iridis.internal.events.annotations.SingleMixin;
import net.devtech.nanoevents.api.Logic;
import net.devtech.nanoevents.api.annotations.Invoker;
import v1_16_1.net.minecraft.server.network.IServerPlayerEntity;

import net.minecraft.text.Text;

@SuppressWarnings ("ReferenceToMixin")
public class PlayerEvents {
	@Invoker (value = "iridis:pre_player_join", args = SingleMixin.class)
	@SingleMixin (PlayerManagerMixin_PrePlayerJoinEvent.class)
	public static Text prePlayerJoin(OfflinePlayer player) {
		Logic.start();
		Text text = prePlayerJoin(player);
		if (text != null) {
			return text;
		}
		Logic.end();
		return null;
	}

	@Invoker (value = "iridis:post_player_join", args = SingleMixin.class)
	@SingleMixin (PlayerManagerMixin_PostPlayerJoinEvent.class)
	public static void postPlayerJoin(IServerPlayerEntity entity) {
		Logic.start();
		postPlayerJoin(entity);
		Logic.end();
	}

	@Invoker (value = "iridis:player_spawn", args = SingleMixin.class)
	@SingleMixin (ServerPlayerEntityMixin_PlayerSpawnEvent.class)
	public static void postPlayerSpawn(IServerPlayerEntity entity) {
		Logic.start();
		postPlayerSpawn(entity);
		Logic.end();
	}
}
