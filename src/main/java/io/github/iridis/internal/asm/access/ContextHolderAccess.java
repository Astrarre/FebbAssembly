package io.github.iridis.internal.asm.access;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * an accessor interface for mananging scheduled ticks
 */
public interface ContextHolderAccess {
	void setContext(String reason, ObjectArrayList<Object> context);

	ObjectArrayList<Object> getContext(String reason);
}
