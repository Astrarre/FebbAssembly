package io.github.iridis.api.player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.mojang.authlib.GameProfile;
import io.github.iridis.api.util.TriId;

public class OfflinePlayer {

	/**
	 * @deprecated internal
	 */
	@Deprecated private final GameProfile profile;

	/**
	 * @deprecated internal
	 */
	@Deprecated
	public OfflinePlayer(Object profile) {
		if (profile instanceof GameProfile) {
			this.profile = (GameProfile) profile;
		} else {
			throw new IllegalArgumentException("profile must be instance of " + GameProfile.class);
		}
	}

	public UUID getId() {
		return this.profile.getId();
	}

	public String getName() {
		return this.profile.getName();
	}

	public List<TriId> getProperty(String name) {
		return this.profile.getProperties()
		                   .get(name)
		                   .stream()
		                   .map(TriId::new)
		                   .collect(Collectors.toList());
	}

	public void putProperty(String name, TriId id) {
		this.profile.getProperties()
		            .get(name)
		            .add(id.toProperty());
	}

	public boolean isLegacy() {
		return this.profile.isLegacy();
	}
}
