package de.teamlapen.vampirism_integrations.graveyard;


import de.teamlapen.lib.lib.util.IInitListener;
import de.teamlapen.vampirism_integrations.VampirismIntegrationsMod;
import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;

public class GraveyardCompat implements IModCompat {

    ForgeConfigSpec.BooleanValue disabled_sundamage_haunted;

    ForgeConfigSpec.BooleanValue disabled_sundamage_other;

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        disabled_sundamage_haunted = builder.comment("Whether sundamage should be applied to vampires in the haunted biomes or not").define("disable_sundamage_haunted", true);
        disabled_sundamage_other = builder.comment("Whether sundamage should be applied to vampires in the other graveyard biomes or not").define("disable_sundamage_other", false);

    }

    @Override
    public String getModID() {
        return "nyctophobia";
    }

    public void onInitStep(IInitListener.Step step, ParallelDispatchEvent event) {
        if (step == Step.COMMON_SETUP) {
            try {
                Biomes.registerNoSundamageBiomes(this);
            } catch (Exception e) {
                VampirismIntegrationsMod.LOGGER.error("Failed to disable sundamge for graveyard biomes", e);
            }
        }
    }
}
