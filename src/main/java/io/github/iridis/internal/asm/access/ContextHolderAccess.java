package io.github.iridis.internal.asm.access;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * an accessor interface for mananging scheduled ticks, entities and stuff like that
 */
public interface ContextHolderAccess {
	void setContext(ObjectArrayList<Object> context);

	ObjectArrayList<Object> getContext();
}
