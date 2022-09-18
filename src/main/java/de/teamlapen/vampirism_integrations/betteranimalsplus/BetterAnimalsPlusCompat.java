package de.teamlapen.vampirism_integrations.betteranimalsplus;

import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;

public class BetterAnimalsPlusCompat implements IModCompat {

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {

    }

    @Override
    public void onInitStep(Step step, ParallelDispatchEvent event) {
        if (step == Step.ENQUEUE_IMC) {
            BetterAnimalsPlusConvertibles.registerConvertibles();
        }
    }

    @Override
    public String getModID() {
        return "betteranimalsplus";
    }
}
