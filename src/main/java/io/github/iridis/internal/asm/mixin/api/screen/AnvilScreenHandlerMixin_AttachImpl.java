package io.github.iridis.internal.asm.mixin.api.screen;

import io.github.iridis.internal.merge.IAnvilScreenHandlerAttach;
import io.github.iridis.internal.merge.IForgingScreenHandlerAttach;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin_AttachImpl extends ForgingScreenHandlerMixin_AttachImpl implements IAnvilScreenHandlerAttach {
	@Shadow private int repairItemUsage;

	@Shadow private String newItemName;

	@Shadow @Final private Property levelCost;

	@Override
	public int getRepairItemUsage() {
		return this.repairItemUsage;
	}

	@Override
	public void setRepairItemUsage(int repairItemUsage) {
		this.repairItemUsage = repairItemUsage;
	}

	@Override
	public String getItemNameText() {
		return this.newItemName;
	}

	@Override
	public void setItemNameText(String text) {
		this.newItemName = text;
	}

	@Override
	public int getLevelCost() {
		return this.levelCost.get();
	}

	@Override
	public void setLevelCost(int cost) {
		this.levelCost.set(cost);
	}
}
