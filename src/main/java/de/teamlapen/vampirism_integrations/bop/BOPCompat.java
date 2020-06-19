package de.teamlapen.vampirism_integrations.bop;

import de.teamlapen.vampirism_integrations.IModCompat;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

public class BOPCompat implements IModCompat {

    public static final String ID = "biomesoplenty";
    static ForgeConfigSpec.DoubleValue conversion_factor;
    ForgeConfigSpec.BooleanValue disabled_sundamage_ominous_woods;

    @Override
    public String getModID() {
        return ID;
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        disabled_sundamage_ominous_woods = builder.comment("Whether sundamage should be applied to vampires in this biome or not").define("disable_sundamage_ominous_woods", true);
        conversion_factor = builder.comment("BOP Hell Blood * factor = Vampirism Blood").defineInRange("hell_blood_conversion_factor", 0.4, 0d, 10d);
    }


    @Override
    public void onInitStep(Step step, ModLifecycleEvent event) {
        if (step == Step.COMMON_SETUP) {
            Biomes.registerNoSundamageBiomes(this);
        }
    }
}
