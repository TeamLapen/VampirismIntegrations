package de.teamlapen.vampirism_integrations.betteranimalsplus;

import de.teamlapen.lib.lib.util.IModCompat;
import de.teamlapen.vampirism_integrations.IInterModeEnqueue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;

import de.teamlapen.lib.lib.util.IInitListener.Step;

public class BetterAnimalsPlusCompat implements IModCompat, IInterModeEnqueue {

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {

    }

    @Override
    public void onInitStep(Step step, ParallelDispatchEvent event) {
    }

    public void enqueueIMC(InterModEnqueueEvent event) {
        BetterAnimalsPlusConvertibles.registerConvertibles();
    }

    @Override
    public String getModID() {
        return "betteranimalsplus";
    }
}
