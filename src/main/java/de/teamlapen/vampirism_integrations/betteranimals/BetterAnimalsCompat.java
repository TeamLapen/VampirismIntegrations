package de.teamlapen.vampirism_integrations.betteranimals;

import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;

public class BetterAnimalsCompat implements IModCompat {

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {

    }

    @Override
    public String getModID() {
        return "betteranimals";
    }

    @Override
    public void onInitStep(Step step, ParallelDispatchEvent event) {
        if (step == Step.ENQUEUE_IMC) {
            BetterAnimalsConvertibles.changeConvertibles();
        }
    }
}
