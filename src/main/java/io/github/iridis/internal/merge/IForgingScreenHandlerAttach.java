package io.github.iridis.internal.merge;

import v1_16_1.net.minecraft.inventory.IInventory;
import v1_16_1.net.minecraft.item.IItemStack;

public interface IForgingScreenHandlerAttach {
	/**
	 * @return inputs for the anvil/smithing table
	 */
	IInventory getInputs();

	default IItemStack getOutput() {
		return this.getOutputs()
		           .getStack(0);
	}

	default void setOutput(IItemStack stack) {
		this.getOutputs()
		    .setStack(0, stack);
	}

	/**
	 * @return output(s) for the anvil/smithing table
	 */
	IInventory getOutputs();
}
