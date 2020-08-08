package io.github.iridis.internal.asm.mixin.api.context;

import java.util.UUID;

import io.github.iridis.api.context.ContextManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;
import net.minecraft.world.World;

@Mixin (TntEntity.class)
public class TntEntityMixin_Test {
	@Inject (method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/entity/LivingEntity;)V", at = @At ("TAIL"))
	private void init(World world, double x, double y, double z, @Nullable LivingEntity igniter, CallbackInfo ci) {
		if(!world.isClient) {
			ContextManager.getInstance()
			              .printStack();
			world.getServer().getPlayerManager().broadcastChatMessage(new LiteralText("Minecraft blamed " + igniter + ", Iridis' context manager blames: " + ContextManager.getInstance()
			                                                                                                                                                               .peekFirstOfType(
					PlayerEntity.class)), MessageType.CHAT, UUID.randomUUID());
		}
	}
}
