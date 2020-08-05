package io.github.iridis.test.events;

import java.util.Random;

import io.github.iridis.internal.events.annotations.Entities;
import net.devtech.nanoevents.annotations.Listener;
import v1_16_1.net.minecraft.block.IBlockState;
import v1_16_1.net.minecraft.block.entity.IBlockEntity;
import v1_16_1.net.minecraft.entity.IEntity;
import v1_16_1.net.minecraft.entity.ILivingEntity;
import v1_16_1.net.minecraft.entity.damage.IDamageSource;
import v1_16_1.net.minecraft.entity.decoration.IArmorStandEntity;
import v1_16_1.net.minecraft.entity.player.IPlayerEntity;
import v1_16_1.net.minecraft.server.IMinecraftServer;
import v1_16_1.net.minecraft.util.math.IBlockPos;
import v1_16_1.net.minecraft.world.IWorld;

/**
 * Test all events Open a world Hit a living entity / armorstand Break a block ( a few times ) close the world
 */
public class Listeners {
	public static final Random RANDOM = new Random();

	@Listener (value = "iridis:entity_damage", args = Entities.class)
	@Entities ({
			ILivingEntity.class,
			IArmorStandEntity.class
	})
	public static boolean entityDamage(IEntity entity, IDamageSource source, float damage) {
		System.out.println("Entity Damage Event Success! " + entity);
		return true;
	}

	@Listener ("iridis:pre_block_break")
	public static boolean playerPreBreak(IPlayerEntity player, IWorld world, IBlockPos pos, IBlockState state, IBlockEntity entity) {
		System.out.println("Player Pre Break Success!" + player.getEntityName());
		boolean rand = RANDOM.nextBoolean();
		if (!rand) {
			System.out.println("Post Event Should Fire");
			return false;
		}
		return true;
	}

	@Listener ("iridis:post_block_break")
	public static void playerPostBreak(IPlayerEntity player, IWorld world, IBlockPos pos, IBlockState state, IBlockEntity entity) {
		System.out.println("Player Post Break Success!" + player.getEntityName());
	}

	@Listener ("iridis:server_start")
	public static void start(IMinecraftServer server) {
		System.out.println("Server start! " + server);
	}

	@Listener ("iridis:server_tick")
	public static void tick(IMinecraftServer server) {
		if (server.getTicks() % 40 == 0) {
			System.out.println("Server tick! " + server);
		}
	}

	@Listener ("iridis:server_stop")
	public static void stop(IMinecraftServer server) {
		System.out.println("Server stop! " + server);
	}
}
