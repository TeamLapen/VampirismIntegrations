package de.teamlapen.vampirism_integrations.bop;

import de.teamlapen.vampirism_integrations.IModCompat;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLStateEvent;

public class BOPCompat implements IModCompat {

    public static final String ID = "biomesoplenty";
    static float conversion_factor = 0.3f;
    boolean disabled_sundamage_ominous_woods;

    @Override
    public String getModID() {
        return ID;
    }

    @Override
    public void loadConfigs(Configuration config, ConfigCategory category) {
        disabled_sundamage_ominous_woods = config.getBoolean("disable_sundamage_ominous_woods", category.getName(), true, "Whether sundamage should be applied to vampires in this biome or not");
        conversion_factor = config.getFloat("hell_blood_conversion_factor", category.getName(), 0.4f, 0.0f, 10f, "BOP Hell Blood * factor = Vampirism Blood");
    }

    @Override
    public void onInitStep(Step step, FMLStateEvent event) {
        if (step == Step.POST_INIT) {
            Biomes.registerNoSundamageBiomes(this);
        }
    }
}
