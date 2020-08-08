package io.github.iridis.internal.asm.mixin.api.context.syncedBlockEvents;

import io.github.iridis.api.context.ContextManager;
import io.github.iridis.internal.asm.access.ContextHolderAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
	@ModifyArg(method = "addSyncedBlockEvent", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectLinkedOpenHashSet;add(Ljava/lang/Object;)Z"))
	private Object add(Object object) {
		((ContextHolderAccess)object).setContext(ContextManager.getInstance().copyStack());
		return object;
	}

	@Redirect (method = "processBlockEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onSyncedBlockEvent(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;II)Z"))
	private boolean process(BlockState state, World world, BlockPos pos, int type, int data, BlockEvent event) {
		return ContextManager.getInstance().actStack(((ContextHolderAccess)event).getContext(), () -> state.onSyncedBlockEvent(world, pos, type, data));
	}
}
