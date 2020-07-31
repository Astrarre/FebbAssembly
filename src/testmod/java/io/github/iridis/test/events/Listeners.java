package io.github.iridis.test.events;

import v1_16_1.net.minecraft.entity.IEntity;
import v1_16_1.net.minecraft.entity.damage.IDamageSource;

public class Listeners {
	public static boolean entityDamage(IEntity entity, IDamageSource source, float damage) {
		System.out.println("iritat");
		return true;
	}
}
