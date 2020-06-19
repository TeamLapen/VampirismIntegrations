package de.teamlapen.vampirism_integrations;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;


public class Config {

    public static void buildConfiguration() {
        ForgeConfigSpec spec = new ForgeConfigSpec.Builder().configure(Config::buildConfiguration).getRight();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, spec);
    }

    private static Object buildConfiguration(ForgeConfigSpec.Builder builder) {
        VampirismIntegrationsMod.instance.compatLoader.buildConfig(builder);
        return null;
    }
}
