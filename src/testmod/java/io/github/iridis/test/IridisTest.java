package io.github.iridis.test;

import net.devtech.nanoevents.NanoEvents;

import net.fabricmc.api.ModInitializer;

public class IridisTest implements ModInitializer {
	@Override
	public void onInitialize() {
		System.out.println("Start!" + NanoEvents.TARGETS);
	}
}
