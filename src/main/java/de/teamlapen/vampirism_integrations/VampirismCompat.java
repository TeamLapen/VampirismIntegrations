package de.teamlapen.vampirism_integrations;

import de.teamlapen.vampirism_integrations.util.REFERENCE;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nullable;

/**
 * Dummy integration for Vampirism itself
 */
public class VampirismCompat implements IModCompat {

    public static ForgeConfigSpec.BooleanValue disableVersionCheck;

    @Override
    public String getModID() {
        return REFERENCE.VAMPIRISM_ID;
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        disableVersionCheck = builder.define("disable_integrations_version_check", false);
    }

    @Nullable
    @Override
    public String getAcceptedVersionRange() {
        return "[1.6.0,)";
    }


}
