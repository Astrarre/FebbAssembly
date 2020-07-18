package io.github.ladder;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import v1_16_1.net.minecraft.block.IBlock;
import v1_16_1.net.minecraft.block.entity.IBlockEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FabricAsm implements Runnable {


    private static Properties loadMcToApi() {
        Properties manifest = new Properties();
        try (InputStream stream = FabricAsm.class.getClassLoader().getResourceAsStream("runtimeManifest.properties")) {
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

//        IBlockEntity

        boolean isDev = FabricLoader.getInstance().isDevelopmentEnvironment();
        mcToApi.forEach((intMcName, apiName) -> {
            String classNameInCurrentRuntime = isDev ? FabricLoader.getInstance().getMappingResolver()
                    .mapClassName("intermediary", intMcName.toString()) : intMcName.toString();

            ClassTinkerers.addTransformation(classNameInCurrentRuntime, (clazz) -> clazz.interfaces.add(apiName.toString()));
        });

        System.out.println("Ladder transformation complete!");


    }
}
