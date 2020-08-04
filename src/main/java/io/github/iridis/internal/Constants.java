package io.github.iridis.internal;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public class Constants {
	public static final MappingResolver MAPPING_RESOLVER = FabricLoader.getInstance().getMappingResolver();
	public static final String ENTITY_CLASS_NAME = MAPPING_RESOLVER.mapClassName("intermediary", "net.minecraft.class_1297");
	public static final String DAMAGE_METHOD_NAME = MAPPING_RESOLVER.mapMethodName("intermediary", "net.minecraft.class_1297", "method_5643", "(Lnet/minecraft/class_1282;F)Z");
	public static final String DAMAGE_SOURCE_CLASS_NAME = MAPPING_RESOLVER.mapClassName("intermediary", "net.minecraft.class_1282");
	public static final String DAMAGE_METHOD_DESC = "(L"+DAMAGE_SOURCE_CLASS_NAME+";F)Z";
	public static final String ENTITY_DAMAGE_EVENTS_INVOKER = "io.github.iridis.internal.invokers.EntityEvents";
	public static final String ENTITY_DAMAGE_LISTENER_DESC = "(L%s;Lv1_16_1/net/minecraft/entity/damage/IDamageSource;F)Z";
}
