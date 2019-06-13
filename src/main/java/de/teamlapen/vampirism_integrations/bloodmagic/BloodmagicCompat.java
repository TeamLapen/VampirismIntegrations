package de.teamlapen.vampirism_integrations.bloodmagic;

import de.teamlapen.vampirism_integrations.IModCompat;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLStateEvent;

public class BloodmagicCompat implements IModCompat {

    static float conversion_factor = 0.8f;

    @Override
    public String getModID() {
        return "bloodmagic";
    }

    @Override
    public void loadConfigs(Configuration config, ConfigCategory category) {
        conversion_factor = config.getFloat("life_essence_conversion_factor", category.getName(), 0.8f, 0.0f, 10, "Blood Magic Life Essence * factor = Vampirism blood");
    }

    @Override
    public void onInitStep(Step step, FMLStateEvent event) {
        if (step == Step.POST_INIT) {
            BloodmagicBloodConversion.registerBloodConversion();
        }
    }
}
