package de.teamlapen.vampirism_integrations.abyssalcraft;

import de.teamlapen.vampirism_integrations.IModCompat;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLStateEvent;

public class AbyssalcraftCompat implements IModCompat {

    public final static String ID = "abyssalcraft";

    boolean disableSundamage_darklands;

    @Override
    public String getModID() {
        return ID;
    }

    @Override
    public void loadConfigs(Configuration config, ConfigCategory category) {
        disableSundamage_darklands = config.getBoolean("disabled_sundamage_darklands", category.getName(), true, "Whether to disable the sundamage in the darkland biomes or not");
    }

    @Override
    public void onInitStep(Step step, FMLStateEvent event) {
        if (step == Step.POST_INIT) {
            Biomes.registerNoSundamageBiomes(this);
        }
    }
}
