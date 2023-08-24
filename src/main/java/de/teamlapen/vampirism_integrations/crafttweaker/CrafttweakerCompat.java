package de.teamlapen.vampirism_integrations.crafttweaker;

import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.minecraftforge.common.ForgeConfigSpec;

public class CrafttweakerCompat implements IModCompat {


    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {

    }

    @Override
    public String getModID() {
        return "crafttweaker";
    }
}
