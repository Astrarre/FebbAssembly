package io.github.iridis.internal.invokers.eh;

import net.devtech.nanoevents.api.event.EventApplyManager;
import net.devtech.nanoevents.util.Id;
import org.ini4j.Profile;

import net.fabricmc.loader.api.FabricLoader;

/**
 * prevents the mixins for an event from being applied if a certain mod is loaded
 * eg. if Fabric API has an event, don't use our mixins, use fabric api's event (better for mod compat)
 */
public class ModEventApplyManager extends EventApplyManager.Default {
	@Override
	public void init(Id id, Profile.Section section) {
		String fapi = section.get("mods");
		for (String s : fapi.split(",")) {
			if(FabricLoader.getInstance().isModLoaded(s)) {
				section.put("mixinPath", null);
				break;
			}
		}
		super.init(id, section);
	}
}
