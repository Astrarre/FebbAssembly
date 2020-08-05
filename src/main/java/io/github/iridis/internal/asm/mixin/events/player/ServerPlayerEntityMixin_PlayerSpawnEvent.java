package io.github.iridis.internal.asm.mixin.events.player;

import io.github.iridis.internal.events.PlayerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import v1_16_1.net.minecraft.server.network.IServerPlayerEntity;

import net.minecraft.server.network.ServerPlayerEntity;

@Mixin (ServerPlayerEntity.class)
public class ServerPlayerEntityMixin_PlayerSpawnEvent {
	@Inject (method = "onSpawn", at = @At ("RETURN"))
	public void onSpawn(CallbackInfo ci) {
		PlayerEvents.postPlayerSpawn((IServerPlayerEntity) this);
	}
}
