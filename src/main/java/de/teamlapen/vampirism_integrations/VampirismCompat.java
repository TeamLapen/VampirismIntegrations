package de.teamlapen.vampirism_integrations;

import de.teamlapen.vampirism_integrations.util.IModCompat;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import net.neoforged.neoforge.common.ModConfigSpec;

import javax.annotation.Nullable;

/**
 * Dummy integration for Vampirism itself
 */
public class VampirismCompat implements IModCompat {


    @Override
    public String getModID() {
        return REFERENCE.VAMPIRISM_ID;
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
    }

    @Nullable
    @Override
    public String getAcceptedVersionRange() {
        return "[1.8.0,)";
    }


}
