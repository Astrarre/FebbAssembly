package io.github.iridis.internal.asm.mixin.events.block;

import io.github.iridis.internal.events.BlockEvents;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import v1_16_1.net.minecraft.screen.IAnvilScreenHandler;

import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin_PreAnvilRecipeEvent extends ScreenHandler {
	protected AnvilScreenHandlerMixin_PreAnvilRecipeEvent(@Nullable ScreenHandlerType<?> type, int syncId) {
		super(type, syncId);
	}

	@Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
	private void update(CallbackInfo ci) {
		if(BlockEvents.preAnvilRecipe((IAnvilScreenHandler) this)) {
			this.sendContentUpdates();
			ci.cancel();
		}
	}
}
