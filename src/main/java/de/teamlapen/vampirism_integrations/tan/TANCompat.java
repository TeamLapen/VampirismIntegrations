package de.teamlapen.vampirism_integrations.tan;

import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.neoforged.fml.event.lifecycle.ParallelDispatchEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;

public class TANCompat implements IModCompat {

    static ModConfigSpec.BooleanValue disableThirst;

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        disableThirst = builder.comment("Limit thirst for vampires").define("disableThirst", true);
    }

    @Override
    public String getModID() {
        return "toughasnails";
    }

    @Override
    public void onInitStep(Step step, ParallelDispatchEvent event) {
        if (step == Step.CLIENT_SETUP) {
            NeoForge.EVENT_BUS.register(new OverlayHandler());
        } else if (step == Step.LOAD_COMPLETE) {
            TemperatureModifier.register();
            NeoForge.EVENT_BUS.register(new ThirstHandler());
        }
    }
}
