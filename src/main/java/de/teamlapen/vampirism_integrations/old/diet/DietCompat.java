package de.teamlapen.vampirism_integrations.old.diet;

import de.teamlapen.vampirism_integrations.util.IModCompat;

import net.neoforged.fml.event.lifecycle.ParallelDispatchEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;

public class DietCompat implements IModCompat {

    static ModConfigSpec.BooleanValue disableDiet;

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        disableDiet = builder.comment("Disable diet functionality for vampires").define("disableVampireDiet", true);
    }

    @Override
    public String getModID() {
        return "diet";
    }

    @Override
    public void onInitStep(Step step, ParallelDispatchEvent event) {
        if (step == Step.COMMON_SETUP) {
            NeoForge.EVENT_BUS.addListener(DietEventHandler::onLevelChanged);
        }
    }
}
