package de.teamlapen.vampirism_integrations.tconstruct;

import de.teamlapen.lib.lib.util.IModCompat;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLStateEvent;

public class TConstructCompat implements IModCompat {
    @Override
    public String getModID() {
        return "tconstruct";
    }

    @Override
    public void loadConfigs(Configuration config, ConfigCategory category) {

    }

    @Override
    public void onInitStep(Step step, FMLStateEvent event) {
        if (step == Step.POST_INIT) {
            TConstructBloodConversion.registerBloodConversion();
        }
    }
}
