package de.teamlapen.vampirism_integrations.tan;

import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;

public class TANCompat implements IModCompat {

    static ForgeConfigSpec.BooleanValue disableThirst;

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        disableThirst = builder.comment("Limit thirst for vampires").define("disableThirst", true);
    }

    @Override
    public String getModID() {
        return "toughasnails";
    }

    @Override
    public void onInitStep(Step step, ParallelDispatchEvent event) {
        if (step == Step.COMMON_SETUP) {
            MinecraftForge.EVENT_BUS.register(new ThirstHandler());
            TemperatureModifier.register();
        }
    }
}
