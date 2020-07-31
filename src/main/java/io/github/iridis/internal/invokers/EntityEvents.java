package io.github.iridis.internal.invokers;

import net.devtech.nanoevents.api.Logic;
import net.devtech.nanoevents.api.annotations.Invoker;
import v1_16_1.net.minecraft.entity.IEntity;
import v1_16_1.net.minecraft.entity.damage.IDamageSource;

public class EntityEvents {
	@Invoker("iridis:pre_damage")
	public static boolean preDamageEvent(IEntity entity, IDamageSource source, float amount) {
		Logic.start();
		if(preDamageEvent(entity, source, amount)) {
			return true;
		}
		Logic.end();
		return false;
	}

	@Invoker("iridis:post_damage")
	public static void postDamageEvent(IEntity entity, IDamageSource source, float amount) {
		Logic.start();
		postDamageEvent(entity, source, amount);
		Logic.end();
	}
}
