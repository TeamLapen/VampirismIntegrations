package de.teamlapen.vampirism_integrations.survive;

import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;

public class SurviveCompat implements IModCompat {

    static ForgeConfigSpec.BooleanValue disableThirstForVampires;
    static ForgeConfigSpec.BooleanValue enableTemperatureVampires;
    static ForgeConfigSpec.BooleanValue enableStaminaBoostVampires;

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
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
                MinecraftForge.EVENT_BUS.register(new ThirstHandler());
            } catch (Exception e) {
                LogManager.getLogger().error("Failed to register survive thirst handler", e);
            }
        }
    }
}
