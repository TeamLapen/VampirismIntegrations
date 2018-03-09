package de.teamlapen.vampirism_integrations.bloodmagic;

import de.teamlapen.lib.lib.util.IModCompat;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLStateEvent;

public class BloodmagicCompat implements IModCompat {


    @Override
    public String getModID() {
        return "bloodmagic";
    }

    @Override
    public void loadConfigs(Configuration config, ConfigCategory category) {

    }

    @Override
    public void onInitStep(Step step, FMLStateEvent event) {
        if (step == Step.POST_INIT) {
            BloodmagicBloodConversion.registerBloodConversion();
        }
    }
}
