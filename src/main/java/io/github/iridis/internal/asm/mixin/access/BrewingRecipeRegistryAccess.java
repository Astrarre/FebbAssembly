package io.github.iridis.internal.asm.mixin.access;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;

@Mixin (BrewingRecipeRegistry.class)
public interface BrewingRecipeRegistryAccess {
	@Invoker
	static void callRegisterItemRecipe(Item input, Item ingredient, Item output) {}

	@Invoker
	static void callRegisterPotionType(Item item) {}

	@Invoker
	static void callRegisterPotionRecipe(Potion input, Item item, Potion output) {}

	// todo
}
