package io.github.iridis.internal.asm.mixin.api.item;

import io.github.iridis.internal.api.item.BurnTimeRegistry;
import io.github.iridis.internal.merge.IItemSettingsAttach;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.item.Item;

@Mixin(Item.class)
public class ItemMixin_RegisterBurnTime {
	@Inject(method = "<init>", at = @At("RETURN"))
	private void init(Item.Settings settings, CallbackInfo ci) {
		if(settings instanceof IItemSettingsAttach) {
			int burnTime = ((IItemSettingsAttach) settings).burnTime();
			if(burnTime != 0) {
				BurnTimeRegistry.put((Item) (Object) this, burnTime);
			}
		} else throw new IllegalStateException("IItemSettingsAttach not implemented (mixin wasn't applied?)");
	}
}
