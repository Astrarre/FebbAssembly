package io.github.iridis.internal;

import com.chocohead.mm.api.ClassTinkerers;
import io.github.iridis.Iridis;

import java.io.IOException;
import java.io.InputStream;
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

    @Override
    public void run() {
        if (!Iridis.IN_DEV) {
            System.out.println("Attaching Iridis API to Minecraft...");
            final Properties mcToApi = getRuntimeManifest();
            mcToApi.forEach((intMcName, apiName) -> {
                String classNameInCurrentRuntime = intMcName.toString();
                ClassTinkerers.addTransformation(classNameInCurrentRuntime, (clazz) -> clazz.interfaces.add(apiName.toString()));
            });
            System.out.println("Iridis transformation complete!");
        }

    }
}
