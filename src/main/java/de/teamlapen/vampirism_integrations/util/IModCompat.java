package de.teamlapen.vampirism_integrations.util;


import de.teamlapen.lib.lib.util.IInitListener;
import net.neoforged.neoforge.common.ModConfigSpec;

import javax.annotation.Nullable;

/**
 * Handles compatibility for a single mod.
 * Should not load any classes outside of init
 */
public interface IModCompat extends IInitListener {
    void buildConfig(ModConfigSpec.Builder builder);

    /**
     * Can be null if all versions are accepted
     * {@link org.apache.maven.artifact.versioning.VersionRange} String
     */
    @SuppressWarnings("SameReturnValue")
    @Nullable
    default String getAcceptedVersionRange() {
        return null;
    }

    String getModID();
}