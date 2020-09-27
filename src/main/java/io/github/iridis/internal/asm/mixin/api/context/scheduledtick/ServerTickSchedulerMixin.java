package io.github.iridis.internal.asm.mixin.api.context.scheduledtick;

import io.github.iridis.api.context.DefaultContext;
import io.github.iridis.internal.asm.access.ContextHolderAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.world.ServerTickScheduler;
import net.minecraft.world.ScheduledTick;

@Mixin(ServerTickScheduler.class)
public abstract class ServerTickSchedulerMixin {
	@Shadow protected abstract void addScheduledTick(ScheduledTick scheduledTick);

	/**@Inject (method = "copyScheduledTicks", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerTickScheduler;addScheduledTick(Lnet/minecraft/world/ScheduledTick;)V"))
	private ScheduledTick<?> scheduledTick(ScheduledTick<?> tick) {
		todo this is only used in /clone so it's of low importance
		todo mix into SimpleTickScheduler#scheduleTo and #fromNbt and ServerTickScheduler#serializeScheduledTicks
	}**/

	@Redirect (method = "schedule", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerTickScheduler;addScheduledTick(Lnet/minecraft/world/ScheduledTick;)V"))
	private <T> void addContext(ServerTickScheduler<T> scheduler, ScheduledTick<T> scheduledTick) {
		((ContextHolderAccess)scheduledTick).setContext(
		                                                DefaultContext.BLAME.get()
		                                                              .copyStack());
		this.addScheduledTick(scheduledTick);
	}
}
