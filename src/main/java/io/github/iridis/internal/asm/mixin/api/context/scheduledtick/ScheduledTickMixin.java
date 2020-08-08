package io.github.iridis.internal.asm.mixin.api.context.scheduledtick;

import io.github.iridis.internal.asm.access.ContextHolderAccess;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.ScheduledTick;

@Mixin(ScheduledTick.class)
public class ScheduledTickMixin implements ContextHolderAccess {
	private ObjectArrayList<Object> context;

	@Override
	public void setContext(String reason, ObjectArrayList<Object> context) {
		if(reason != null) throw new IllegalStateException("Scheduled ticks only store the stack from when they're created, so pass null for reason");
		this.context = context;
	}

	@Override
	public ObjectArrayList<Object> getContext(String reason) {
		return this.context;
	}
}
