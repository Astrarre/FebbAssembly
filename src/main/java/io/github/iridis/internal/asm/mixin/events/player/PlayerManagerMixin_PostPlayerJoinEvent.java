package io.github.iridis.internal.asm.mixin.events.player;

import io.github.iridis.internal.events.PlayerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import v1_16_1.net.minecraft.server.network.IServerPlayerEntity;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin (PlayerManager.class)
public class PlayerManagerMixin_PostPlayerJoinEvent {
	@Inject (method = "onPlayerConnect",
			at = @At (value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;onSpawn()V"),
			locals = LocalCapture.PRINT)
	private static void postConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		PlayerEvents.postPlayerJoin((IServerPlayerEntity) player);
	}
}
