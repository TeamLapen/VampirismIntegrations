package de.teamlapen.vampirism_integrations.consecration;

import de.teamlapen.vampirism_integrations.IModCompat;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;


public class ConsecrationCompat implements IModCompat {
    public static final String ID = "consecration";

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {

    }

    @Override
    public String getModID() {
        return ID;
    }

    @Override
    public void onInitStep(Step step, ParallelDispatchEvent event) {
        if (step == Step.COMMON_SETUP) {
            MinecraftForge.EVENT_BUS.addListener(HolyRegistration::registerToHolyRegistry);
        }
    }
}
