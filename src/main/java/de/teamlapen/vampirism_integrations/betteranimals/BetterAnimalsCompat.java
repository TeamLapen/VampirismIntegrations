package de.teamlapen.vampirism_integrations.betteranimals;

import de.teamlapen.lib.lib.util.IModCompat;
import de.teamlapen.vampirism_integrations.IInterModeEnqueue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

public class BetterAnimalsCompat implements IModCompat, IInterModeEnqueue {

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {

    }

    @Override
    public String getModID() {
        return "betteranimals";
    }

    @Override
    public void enqueueIMC(InterModEnqueueEvent event) {
        BetterAnimalsConvertibles.changeConvertibles();
    }
}
