package de.teamlapen.vampirism_integrations.old.consecration;

import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.neoforged.fml.event.lifecycle.ParallelDispatchEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ConsecrationCompat implements IModCompat {
    public static final String ID = "consecration";

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {

    }

    @Override
    public String getModID() {
        return ID;
    }

    @Override
    public void onInitStep(Step step, ParallelDispatchEvent event) {
    }
}
