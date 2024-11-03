package de.teamlapen.vampirism_integrations.survive;

import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.neoforged.fml.event.lifecycle.ParallelDispatchEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;

public class SurviveCompat implements IModCompat {

    static ModConfigSpec.BooleanValue disableThirstForVampires;
    static ModConfigSpec.BooleanValue enableTemperatureVampires;
    static ModConfigSpec.BooleanValue enableStaminaBoostVampires;

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        disableThirstForVampires = builder.comment("Whether vampires should not need to drink").define("disableThirstVampires", true);
        enableTemperatureVampires = builder.comment("Grant vampires cold resistance, but decrease heat resistance").define("enableTemperatureVampires", true);
        enableStaminaBoostVampires = builder.comment("Increase natural regeneration for vampires").define("enableStaminaBoostVampires", true);
    }

    @Override
    public String getModID() {
        return "survive";
    }

    @Nullable
    @Override
    public String getAcceptedVersionRange() {
        return "[1.16.5-3.4.4,)";
    }

    @Override
    public void onInitStep(Step step, ParallelDispatchEvent event) {
        if (step == Step.COMMON_SETUP) {
            try {
                NeoForge.EVENT_BUS.register(new SurviveHandler());
            } catch (Exception e) {
                LogManager.getLogger().error("Failed to register survive thirst handler", e);
            }
        }
    }
}
