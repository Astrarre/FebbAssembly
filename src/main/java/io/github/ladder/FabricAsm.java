package io.github.ladder;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FabricAsm implements Runnable {


    private static Properties loadMcToApi() {
        Properties manifest = new Properties();
        try (InputStream stream = FabricAsm.class.getClassLoader().getResourceAsStream("runtime-manifest.properties")) {
            if (stream == null) throw new IllegalStateException("Fuck resources");
            manifest.load(stream);
        } catch (IOException e) {
            throw new IllegalStateException("Could not load runtime manifest for attaching febb api interfaces!", e);
        }
        return manifest;
    }

    @Override
    public void run() {
        System.out.println("Attaching Ladder API to Minecraft...");

        final Properties mcToApi = loadMcToApi();

        boolean isDev = FabricLoader.getInstance().isDevelopmentEnvironment();
        mcToApi.forEach((intMcName, apiName) -> {
            String classNameInCurrentRuntime = isDev ? FabricLoader.getInstance().getMappingResolver()
                    .mapClassName("intermediary", intMcName.toString()) : intMcName.toString();

            ClassTinkerers.addTransformation(classNameInCurrentRuntime, (clazz) -> clazz.interfaces.add(classNameInCurrentRuntime));
        });

        System.out.println("Ladder transformation complete!");


    }
}
