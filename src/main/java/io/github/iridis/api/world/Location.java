package io.github.iridis.api.world;

import java.util.List;
import java.util.function.Predicate;

import v1_16_1.net.minecraft.block.IBlockState;
import v1_16_1.net.minecraft.entity.IEntity;
import v1_16_1.net.minecraft.util.math.IBlockPos;
import v1_16_1.net.minecraft.util.math.IBox;
import v1_16_1.net.minecraft.world.IWorld;

/**
 * A location in the world, a utility object
 */
public class Location {
	private final IWorld world;
	private final IBlockPos pos;
	private IBox cachedBox;

	public Location(IWorld world, IBlockPos pos) {
		this.world = world;
		this.pos = pos;
	}

	public Location(IWorld world, int x, int y, int z) {
		this(world, IBlockPos.create(x, y, z));
	}

	public IBlockState getBlockState() {
		return this.world.getBlockState(this.pos);
	}

	/**
	 * @return a box who's bounds encompasses this block
	 */
	public IBox box() {
		IBox box = this.cachedBox;
		if (box == null) {
			return this.cachedBox = IBox.create(this.pos);
		}
		return box;
	}

	/**
	 * @return a list of entities that are intersecting with the block
	 */
	public List<IEntity> entitiesIn() {
		return this.world.getEntities((IEntity) null, this.box(), null);
	}

	/**
	 * @param predicate a filter for the entities
	 * @return a list of entities that are intersecting with the block
	 */
	public List<IEntity> entitiesIn(Predicate<IEntity> predicate) {
		return this.world.getEntities((IEntity) null, this.box(), predicate);
	}
}
