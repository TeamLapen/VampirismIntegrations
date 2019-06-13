package de.teamlapen.vampirism_integrations.evilcraft;

import de.teamlapen.vampirism_integrations.IModCompat;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLStateEvent;

public class EvilCraftCompat implements IModCompat {
    static float conversionFactor = 0.8f;

    @Override
    public String getModID() {
        return "evilcraft";
    }

    @Override
    public void loadConfigs(Configuration config, ConfigCategory category) {
        conversionFactor = config.getFloat("blood_conversion_factor", category.getName(), 0.8f, 0.0f, 20, "Evilcraft Blood * factor = Vampirism Blood");
    }

    @Override
    public void onInitStep(Step step, FMLStateEvent event) {
        if (step == Step.POST_INIT) {
            EvilCraftBloodConversion.registerBloodConversion();
        }
    }
}
