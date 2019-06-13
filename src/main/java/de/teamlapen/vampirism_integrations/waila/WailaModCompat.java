package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.vampirism_integrations.IModCompat;
import net.minecraft.block.Block;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WailaModCompat implements IModCompat {

    @GameRegistry.ObjectHolder("vampirism:garlic_beacon")
    static Block garlicBeacon;

    @Override
    public String getModID() {
        return "waila";
    }

    @Override
    public void loadConfigs(Configuration config, ConfigCategory category) {

    }

    @Override
    public void onInitStep(Step step, FMLStateEvent event) {
        if (step == Step.INIT) {
            FMLInterModComms.sendMessage(getModID(), "register", WailaHandler.class.getName() + ".onRegister");
        }
    }
}
