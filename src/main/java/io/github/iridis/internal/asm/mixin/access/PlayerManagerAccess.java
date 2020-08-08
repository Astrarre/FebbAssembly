package io.github.iridis.internal.asm.mixin.access;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(PlayerManager.class)
public interface PlayerManagerAccess {
	@Invoker
	void callSavePlayerData(ServerPlayerEntity player);
}
