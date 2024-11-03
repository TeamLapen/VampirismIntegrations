package de.teamlapen.vampirism_integrations.evilcraft;

import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.neoforged.neoforge.common.ModConfigSpec;

public class EvilCraftCompat implements IModCompat {

    @Override
    public String getModID() {
        return "evilcraft";
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {

    }
}
