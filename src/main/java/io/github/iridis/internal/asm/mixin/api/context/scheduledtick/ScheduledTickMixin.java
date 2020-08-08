package io.github.iridis.internal.asm.mixin.api.context.scheduledtick;

import io.github.iridis.internal.asm.access.ContextHolderAccess;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.ScheduledTick;

@Mixin(ScheduledTick.class)
public class ScheduledTickMixin implements ContextHolderAccess {
	private ObjectArrayList<Object> context;

	@Override
	public void setContext(ObjectArrayList<Object> context) {
		this.context = context;
	}

	@Override
	public ObjectArrayList<Object> getContext() {
		return this.context;
	}
}
