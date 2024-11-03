package de.teamlapen.vampirism_integrations.coldsweat;

import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.neoforged.fml.event.lifecycle.ParallelDispatchEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

public class ColdSweatCompat implements IModCompat {
    public static final String ID = "cold_sweat";
    public static ModConfigSpec.BooleanValue enableTemperatureVampires;
    public static ModConfigSpec.DoubleValue vampireColdResistance;
    public static ModConfigSpec.DoubleValue vampireBurningPointModifier;

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        enableTemperatureVampires = builder.comment("Grant vampires cold resistance, but decrease heat resistance").define("enableTemperatureVampires", true);
        vampireColdResistance = builder.comment("Increase cold resistance for vampires by this degree celsius").defineInRange("vampireColdResistance", 20d, 0, 100);
        vampireBurningPointModifier = builder.comment("Decrease the burning point of vampires by this factor").defineInRange("vampireBurningPointModifier", 0.7, 0, 1);
    }

    @Override
    public String getModID() {
        return ID;
    }

    @Nullable
    @Override
    public String getAcceptedVersionRange() {
        return "[2.2.3,)";
    }

    @Override
    public void onInitStep(Step step, ParallelDispatchEvent event) {
        if (step == Step.COMMON_SETUP) {
            try {
                NeoForge.EVENT_BUS.register(new ColdSweatEventHandler());
            } catch (Exception e) {
                LogManager.getLogger().error("Failed to register cold sweat event handler", e);
            }
        }
    }
}
