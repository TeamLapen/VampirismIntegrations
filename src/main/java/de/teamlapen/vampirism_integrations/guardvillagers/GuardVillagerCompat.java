package de.teamlapen.vampirism_integrations.guardvillagers;

import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.neoforged.fml.event.lifecycle.ParallelDispatchEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

public class GuardVillagerCompat implements IModCompat {
    public static final String ID = "guardvillagers";

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {

    }

    @Override
    public void onInitStep(Step step, ParallelDispatchEvent event) {
        if (step == Step.COMMON_SETUP) {
            NeoForge.EVENT_BUS.register(new GuardVillagerEventHandler());
        }
    }

    @Nullable
    @Override
    public String getAcceptedVersionRange() {
        return "[1.20.1-1.6.3,)";
    }

    @Override
    public String getModID() {
        return ID;
    }
}
