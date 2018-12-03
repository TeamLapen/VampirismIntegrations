package de.teamlapen.vampirism_integrations.tan;

import de.teamlapen.lib.lib.util.IModCompat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLStateEvent;

public class TANCompat implements IModCompat {

    final static String MODID = "toughasnails";
    static boolean replace_hyperthermia;
    static boolean replace_hypothermia;
    static boolean thirst_limit;

    @Override
    public String getModID() {
        return MODID;
    }

    @Override
    public void loadConfigs(Configuration config, ConfigCategory category) {
        replace_hyperthermia = config.getBoolean("replace_hyperthermia", category.getName(), true, "");
        replace_hypothermia = config.getBoolean("replace_hypothermia", category.getName(), true, "");
        thirst_limit = config.getBoolean("limit_thirst", category.getName(), true, "");
    }

    @Override
    public void onInitStep(Step step, FMLStateEvent event) {
        if (step == Step.POST_INIT) {
            TANRegistration.registerPotions();
            if (thirst_limit) MinecraftForge.EVENT_BUS.register(new ThirstHandler());
            TANRegistration.registerCampfire();
        }
    }
}
