package io.github.iridis.internal.events.annotations;

import net.devtech.nanoevents.api.annotations.Name;

/**
 * @see io.github.iridis.internal.events.eams.ModEventApplyManager
 */
public @interface ModPresent {
	@Name ("mods") String[] value();
}
