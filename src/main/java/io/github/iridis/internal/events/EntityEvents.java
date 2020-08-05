package io.github.iridis.internal.events;

import io.github.iridis.internal.asm.mixin.events.entity.LivingEntityMixin_PreLivingEntityLandEvent;
import io.github.iridis.internal.events.annotations.SingleMixin;
import net.devtech.nanoevents.api.Logic;
import net.devtech.nanoevents.api.annotations.Invoker;
import org.apache.commons.lang3.mutable.MutableFloat;
import v1_16_1.net.minecraft.block.IBlockState;
import v1_16_1.net.minecraft.entity.ILivingEntity;
import v1_16_1.net.minecraft.util.math.IBlockPos;

@SuppressWarnings ("ReferenceToMixin")
public class EntityEvents {
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
	 * @see #preLivingEnitityLandEvent(ILivingEntity, float, MutableFloat)
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
