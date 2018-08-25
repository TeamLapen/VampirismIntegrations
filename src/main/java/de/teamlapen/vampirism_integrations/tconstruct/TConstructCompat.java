package de.teamlapen.vampirism_integrations.tconstruct;

import de.teamlapen.lib.lib.util.IModCompat;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLStateEvent;

public class TConstructCompat implements IModCompat {

    static float conversion_factor = 0.8f;
    @Override
    public String getModID() {
        return "tconstruct";
    }

    @Override
    public void loadConfigs(Configuration config, ConfigCategory category) {
        conversion_factor = config.getFloat("blood_conversion_factor", category.getName(), 0.8f, 0.0f, 10, "Tinker blood * factor = Vampirism Blood");
    }

    @Override
    public void onInitStep(Step step, FMLStateEvent event) {
        if (step == Step.POST_INIT) {
            TConstructBloodConversion.registerBloodConversion();
        }
    }
}
