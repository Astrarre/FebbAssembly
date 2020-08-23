package io.github.iridis.internal.events;

import io.github.iridis.internal.asm.mixin.events.entity.LivingEntityMixin_PreLivingEntityLandEvent;
import io.github.iridis.internal.asm.mixin.events.entity.MobEntityMixin_MobEquipEvent;
import io.github.iridis.internal.events.annotations.SingleMixin;
import net.devtech.nanoevents.api.Logic;
import net.devtech.nanoevents.api.annotations.Invoker;
import org.jetbrains.annotations.Nullable;
import v1_16_1.net.minecraft.block.IBlockState;
import v1_16_1.net.minecraft.entity.IEquipmentSlot;
import v1_16_1.net.minecraft.entity.ILivingEntity;
import v1_16_1.net.minecraft.entity.mob.IMobEntity;
import v1_16_1.net.minecraft.item.IItem;
import v1_16_1.net.minecraft.util.math.IBlockPos;

import net.minecraft.entity.mob.MobEntity;

@SuppressWarnings ("ReferenceToMixin")
public class EntityEvents {

	/**
	 * fired when an mob entity is spawned, and equipped with armor, for example if you have a mod that adds a new tier of armor, you would do.
	 * <code>
	 *     if(equipmentLevel == 4 && entity.getRandom().nextFloat() < .095) {
	 *         switch(slot) {
	 *             case HEAD:
	 *              return MyMod.SPECIAL_HELMET;
	 *             ...
	 *             default:
	 *              return null;
	 *         }
	 *     }
	 * </code>
	 * This is invoked after vanilla logic.
	 *
	 * @param equipmentLevel the current max equipment level (4 vanilla)
	 * @return the max equipment level
	 */
	@Invoker(value = "iridis:mob_equip_event", args = SingleMixin.class)
	@SingleMixin(MobEntityMixin_MobEquipEvent.class)
	public static @Nullable IItem equipmentEvent(IMobEntity entity, IEquipmentSlot slot, int equipmentLevel) {
		Logic.start();
		IItem item = equipmentEvent(entity, slot, equipmentLevel);
		if(item != null) return item;
		Logic.end();
		return null;
	}

	/**
	 * called when a living entity lands on a block
	 *
	 * @return true to cancel the event
	 */
	@Invoker (value = "iridis:pre_entity_fall_event", args = SingleMixin.class)
	@SingleMixin (LivingEntityMixin_PreLivingEntityLandEvent.class)
	public static boolean preLivingEnitityLandEvent(ILivingEntity entity, double heightDifference, boolean onGround, IBlockState landedState,
	                                                IBlockPos landedPosition) {
		Logic.start();
		if (preLivingEnitityLandEvent(entity, heightDifference, onGround, landedState, landedPosition)) {
			return true;
		}
		Logic.end();
		return false;
	}

	/**
	 * post event for
	 *
	 * @see #preLivingEnitityLandEvent(ILivingEntity, double, boolean, IBlockState, IBlockPos)
	 */
	@Invoker (value = "iridis:post_entity_fall_event", args = SingleMixin.class)
	@SingleMixin (LivingEntityMixin_PreLivingEntityLandEvent.class)
	public static void postLivingEntityLandEvent(ILivingEntity entity, double heightDifference, boolean onGround, IBlockState landedState,
	                                             IBlockPos landedPosition) {
		Logic.start();
		postLivingEntityLandEvent(entity, heightDifference, onGround, landedState, landedPosition);
		Logic.end();
	}
}
