package de.teamlapen.vampirism_integrations.crafttweaker;

import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CrafttweakerCompat implements IModCompat {


    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {

    }

    @Override
    public String getModID() {
        return "crafttweaker";
    }
}
