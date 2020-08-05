package io.github.iridis.api.util;

import com.mojang.authlib.properties.Property;
import org.jetbrains.annotations.Nullable;

public class TriId {
	private final String name, value, signature;
	@Deprecated @Nullable private Property property;

	/**
	 * @deprecated internal
	 */
	@Deprecated
	public TriId(Property id) {
		this(id.getName(), id.getValue(), id.getSignature());
	}

	public TriId(String name, String value, @Nullable String signature) {
		this.name = name;
		this.value = value;
		this.signature = signature;
	}

	public String getName() {
		return this.name;
	}

	public String getValue() {
		return this.value;
	}

	@Nullable
	public String getSignature() {
		return this.signature;
	}

	/**
	 * @deprecated internal
	 */
	@Deprecated
	public Property toProperty() {
		if (this.property == null) {
			return this.property = new Property(this.name, this.value, this.signature);
		}
		return this.property;
	}

}
