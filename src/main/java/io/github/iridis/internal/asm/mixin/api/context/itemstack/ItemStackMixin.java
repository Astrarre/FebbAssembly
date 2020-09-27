package io.github.iridis.internal.asm.mixin.api.context.itemstack;

import static io.github.iridis.api.context.ContextKey.of;

import io.github.iridis.api.context.DefaultContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;onStoppedUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V"))
	private void onStopped(Item item, ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		DefaultContext.BLAME.get().act(() -> {
			item.onStoppedUsing(stack, world, user, remainingUseTicks);
			return null;
		}, of("stoppedUsingItem", stack), of("stoppedUsingUser", user));
	}
}
