package de.teamlapen.vampirism_integrations.evilcraft;

import de.teamlapen.lib.lib.util.IModCompat;
import net.minecraftforge.common.ForgeConfigSpec;

public class EvilCraftCompat implements IModCompat {

    @Override
    public String getModID() {
        return "evilcraft";
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {

    }
}
