package de.teamlapen.vampirism_integrations.bop;

import de.teamlapen.lib.lib.util.IInitListener;
import de.teamlapen.vampirism_integrations.VampirismIntegrationsMod;
import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.neoforged.fml.event.lifecycle.ParallelDispatchEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class BOPCompat implements IModCompat {

    public static final String ID = "biomesoplenty";
    ModConfigSpec.BooleanValue disabled_sundamage_ominous_woods;

    @Override
    public String getModID() {
        return ID;
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        disabled_sundamage_ominous_woods = builder.comment("Whether sundamage should be applied to vampires in this biome or not").define("disable_sundamage_ominous_woods", true);
    }


    @Override
    public void onInitStep(IInitListener.Step step, ParallelDispatchEvent event) {
        if (step == Step.COMMON_SETUP) {
            try {
                Biomes.registerNoSundamageBiomes(this);
            } catch (Exception e) {
                VampirismIntegrationsMod.LOGGER.error("Failed to disable sundamage for biomes o plenty biomes", e);
            }
        }
    }
}
