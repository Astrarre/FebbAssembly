package io.github.iridis.internal.events.annotations;

import net.devtech.nanoevents.api.annotations.Name;
import v1_16_1.net.minecraft.entity.IEntity;

public @interface Entities {
	@Name ("entities") Class<? extends IEntity>[] value();
}
