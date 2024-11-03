package de.teamlapen.vampirism_integrations;

import com.electronwill.nightconfig.core.ConfigSpec;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;


public class Config {

    public static void buildConfiguration() {
        ModConfigSpec spec = new ModConfigSpec.Builder().configure(Config::buildConfiguration).getRight();
        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, spec);
    }

    private static Object buildConfiguration(ModConfigSpec.Builder builder) {
        VampirismIntegrationsMod.instance.compatLoader.buildConfig(builder);
        return null;
    }
}
