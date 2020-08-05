package io.github.iridis.internal.asm.mixin.events.player;

import java.net.SocketAddress;

import com.mojang.authlib.GameProfile;
import io.github.iridis.api.player.OfflinePlayer;
import io.github.iridis.internal.events.PlayerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;

@Mixin (PlayerManager.class)
public class PlayerManagerMixin_PrePlayerJoinEvent {
	@Inject (method = "checkCanJoin", at = @At ("HEAD"), cancellable = true)
	private void canJoin(SocketAddress socketAddress, GameProfile gameProfile, CallbackInfoReturnable<Text> cir) {
		Text deny = PlayerEvents.prePlayerJoin(new OfflinePlayer(gameProfile));
		if (deny != null) {
			cir.setReturnValue(deny);
		}
	}
}