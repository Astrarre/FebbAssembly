package io.github.iridis.internal.asm.mixin.api.context.entity;

import io.github.iridis.api.context.ContextKey;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;

@Mixin (MobEntity.class)
public abstract class MobEntityMixin extends EntityMixin {
	@Shadow private LivingEntity target;

	@Override
	public ObjectArrayList<Object> getContext() {
		ObjectArrayList<Object> context = super.getContext();
		if(this.target != null) {
			ObjectArrayList<Object> newContext = context == null ? new ObjectArrayList<>() : context.clone();
			newContext.push(ContextKey.of("mobEntityTarget", this.target));
			return newContext;
		} else {
			return context;
		}
	}
}
