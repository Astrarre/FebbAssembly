package io.github.iridis.test.events;

import java.util.Random;

import v1_16_1.net.minecraft.block.IBlockState;
import v1_16_1.net.minecraft.block.entity.IBlockEntity;
import v1_16_1.net.minecraft.entity.IEntity;
import v1_16_1.net.minecraft.entity.damage.IDamageSource;
import v1_16_1.net.minecraft.entity.player.IPlayerEntity;
import v1_16_1.net.minecraft.util.math.IBlockPos;
import v1_16_1.net.minecraft.world.IWorld;

public class Listeners {
	public static final Random RANDOM = new Random();
	public static boolean entityDamage(IEntity entity, IDamageSource source, float damage) {
		System.out.println("entity damage " + entity);
		return true;
	}

	public static boolean playerPreBreak(IPlayerEntity player, IWorld world, IBlockPos pos, IBlockState state, IBlockEntity entity) {
		System.out.println("player break " + player.getEntityName());
		boolean rand = RANDOM.nextBoolean();
		if(!rand) {
			System.out.println("post should fire");
		}
		return true;
	}

	public static void playerPostBreak(IPlayerEntity player, IWorld world, IBlockPos pos, IBlockState state, IBlockEntity entity) {
		System.out.println("player post break " + player.getEntityName());
	}
}
