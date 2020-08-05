package io.github.iridis.api.data;

/**
 * an arbitrary version for a mod or minecraft
 */
public interface Version {
	/**
	 * @return a user friendly version string
	 */
	@Override
	String toString();

	/**
	 * a minecraft version
	 */
	enum Minecraft implements Version {
		v1_16_1
	}
}
