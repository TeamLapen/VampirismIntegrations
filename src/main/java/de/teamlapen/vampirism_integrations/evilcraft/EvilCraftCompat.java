package de.teamlapen.vampirism_integrations.evilcraft;

import de.teamlapen.lib.lib.util.IModCompat;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLStateEvent;

public class EvilCraftCompat implements IModCompat {
    static float conversionFactor = 1f;

    @Override
    public String getModID() {
        return "evilcraft";
    }

    @Override
    public void loadConfigs(Configuration config, ConfigCategory category) {

    }

    @Override
    public void onInitStep(Step step, FMLStateEvent event) {
        if (step == Step.POST_INIT) {
            EvilCraftBloodConversion.registerBloodConversion();
        }
    }
}
