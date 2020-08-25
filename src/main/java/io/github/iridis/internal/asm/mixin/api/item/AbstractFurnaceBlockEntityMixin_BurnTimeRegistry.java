package io.github.iridis.internal.asm.mixin.api.item;

import java.util.Map;

import io.github.iridis.internal.api.item.BurnTimeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Item;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin_BurnTimeRegistry {
	@ModifyVariable(method = "createFuelTimeMap", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/google/common/collect/Maps;newLinkedHashMap()Ljava/util/LinkedHashMap;"))
	private static Map<Item, Integer> fuelMap(Map<Item, Integer> map) {
		return BurnTimeRegistry.wrap(map);
	}
}