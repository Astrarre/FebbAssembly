package io.github.iridis.api.misc;

import io.github.iridis.internal.asm.mixin.access.BrewingRecipeRegistryAccess;
import v1_16_1.net.minecraft.item.IItem;
import v1_16_1.net.minecraft.potion.IPotion;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;

/**
 * A registry for custom brewing recipes
 */
public interface Brewing {
	/**
	 * register a brewing recipe with raw items
	 */
	static void registerItemRecipe(IItem input, IItem ingredient, IItem output) {
		BrewingRecipeRegistryAccess.callRegisterItemRecipe((Item) input, (Item) ingredient, (Item) output);
	}

	/**
	 * register a potion type, tbh I dunno what this does
	 */
	static void registerPotionType(IItem item) {
		BrewingRecipeRegistryAccess.callRegisterPotionType((Item) item);
	}

	/**
	 * register a potion based recipe, eg. upgrading recipes when u put in 1 redstone and u get a longer lasting potion
	 */
	static void registerPotionRecipe(IPotion input, IItem catalyst, IPotion output) {
		BrewingRecipeRegistryAccess.callRegisterPotionRecipe((Potion) input, (Item) catalyst, (Potion) output);
	}
}
