package io.github.iridis.internal.asm.mixin.events.entity;

import io.github.iridis.internal.events.EntityEvents;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import v1_16_1.net.minecraft.entity.IEquipmentSlot;
import v1_16_1.net.minecraft.entity.mob.IMobEntity;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;

@Mixin (MobEntity.class)
public class MobEntityMixin_MobEquipEvent {
	@Redirect (method = "initEquipment",
			at = @At (value = "INVOKE",
					target = "Lnet/minecraft/entity/mob/MobEntity;getEquipmentForSlot(Lnet/minecraft/entity/EquipmentSlot;I)Lnet/minecraft/item/Item;"))
	private @Nullable Item getEquipmentForSlot(EquipmentSlot equipmentSlot, int equipmentLevel) {
		return (Item) EntityEvents.equipmentEvent((IMobEntity) this, (IEquipmentSlot) (Object) equipmentSlot, equipmentLevel);
	}
}
