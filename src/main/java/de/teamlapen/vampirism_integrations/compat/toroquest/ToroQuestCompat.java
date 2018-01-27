package de.teamlapen.vampirism_integrations.compat.toroquest;

import de.teamlapen.lib.lib.util.IModCompat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLStateEvent;

public class ToroQuestCompat implements IModCompat {
    @Override
    public String getModID() {
        return "toroquest";
    }

    @Override
    public void loadConfigs(Configuration config, ConfigCategory category) {

    }

    @Override
    public void onInitStep(Step step, FMLStateEvent event) {
        if (step == Step.INIT) {
            MinecraftForge.EVENT_BUS.register(new ToroQuestAIHandler());
        }
    }
}
