package io.github.iridis.internal.asm.mixin.api.screen;

import io.github.iridis.internal.merge.IForgingScreenHandlerAttach;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import v1_16_1.net.minecraft.inventory.IInventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ForgingScreenHandler;

@Mixin(ForgingScreenHandler.class)
public class ForgingScreenHandlerMixin_AttachImpl implements IForgingScreenHandlerAttach {
	@Shadow @Final protected Inventory input;

	@Shadow @Final protected Inventory output;

	@Override
	public IInventory getInputs() {
		return (IInventory) this.input;
	}

	@Override
	public IInventory getOutputs() {
		return (IInventory) this.output;
	}
}
