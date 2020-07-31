package io.github.iridis.internal;

import net.fabricmc.loader.api.FabricLoader;

public class Constants {
	public static final String DAMAGE_METHOD_NAME = FabricLoader.getInstance().getMappingResolver().mapMethodName("intermediary", "net/minecraft/class_1297", "method_5643", "(Lnet/minecraft/class_1282;F)Z");
	public static final String DAMAGE_SOURCE_CLASS_NAME = FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net/minecraft/class_1282");
	public static final String DAMAGE_METHOD_DESC = "(L"+DAMAGE_SOURCE_CLASS_NAME+";F)Z";
	public static final String ENTITY_DAMAGE_EVENTS_INVOKER = "io.github.iridis.internal.invokers.EntityEvents";
	public static final String ENTITY_DAMAGE_LISTENER_DESC = "(Lv1_16_1/net/minecraft/entity/IEntity;Lv1_16_1/net/minecraft/entity/damage/IDamageSource;F)Z";
}
