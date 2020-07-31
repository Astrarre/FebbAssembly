package io.github.iridis.internal;

import com.chocohead.mm.api.ClassTinkerers;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.iridis.api.Iridis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class IridisAsm implements Runnable {
    private static Properties getRuntimeManifest() {
        Properties manifest = new Properties();
        try (InputStream stream = IridisAsm.class.getClassLoader().getResourceAsStream("runtimeManifest.properties")) {
            if (stream == null) throw new IllegalStateException("Fuck resources");
            manifest.load(stream);
        } catch (IOException e) {
            throw new IllegalStateException("Could not load runtime manifest for attaching febb api interfaces!", e);
        }
        return manifest;
    }

    private static final BiMap<String, String> MC_TO_API = HashBiMap.create((Map)getRuntimeManifest());

    @Override
    public void run() {
        if (!Iridis.IN_DEV) {
            System.out.println("Attaching Iridis API to Minecraft...");
            MC_TO_API.forEach((intMcName, apiName) -> {
                String classNameInCurrentRuntime = intMcName.toString();
                ClassTinkerers.addTransformation(classNameInCurrentRuntime, (clazz) -> clazz.interfaces.add(apiName.toString()));
            });
            System.out.println("Iridis transformation complete!");
        }

    }

    public static String getMinecraftFromApi(String apiClassName) {
        return MC_TO_API.inverse().get(apiClassName);
    }
}
