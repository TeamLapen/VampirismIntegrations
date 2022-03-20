package de.teamlapen.vampirism_integrations.diet;

import de.teamlapen.lib.lib.util.IModCompat;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;

import de.teamlapen.lib.lib.util.IInitListener.Step;

public class DietCompat implements IModCompat {

    static ForgeConfigSpec.BooleanValue disableDiet;

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        disableDiet = builder.comment("Disable diet functionality for vampires").define("disableVampireDiet", true);
    }

    @Override
    public String getModID() {
        return "diet";
    }

    @Override
    public void onInitStep(Step step, ParallelDispatchEvent event) {
        if (step == Step.COMMON_SETUP) {
            MinecraftForge.EVENT_BUS.addListener(DietEventHandler::onLevelChanged);
        }
    }
}
