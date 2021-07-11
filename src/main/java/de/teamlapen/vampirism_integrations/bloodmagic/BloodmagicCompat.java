package de.teamlapen.vampirism_integrations.bloodmagic;

import de.teamlapen.lib.lib.util.IInitListener;
import de.teamlapen.lib.lib.util.IModCompat;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;

public class BloodmagicCompat implements IModCompat {

    @Override
    public String getModID() {
        return "bloodmagic";
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
    }

    @Override
    public void onInitStep(IInitListener.Step step, ParallelDispatchEvent event) {

    }
}
