package de.teamlapen.vampirism_integrations.jade;

import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Nullable;

public class JadeModCompat implements IModCompat {
    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {

    }

    @Override
    public String getModID() {
        return "jade";
    }

    @Nullable
    @Override
    public String getAcceptedVersionRange() {
        return "[11.6.3,)";
    }
}
