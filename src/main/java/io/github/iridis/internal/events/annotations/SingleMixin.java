package io.github.iridis.internal.events.annotations;

import net.devtech.nanoevents.api.annotations.Name;

public @interface SingleMixin {
	@Name ("mixinPath") Class<?> value();
}
