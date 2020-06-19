package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.vampirism_integrations.IModCompat;
import net.minecraft.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import net.minecraftforge.registries.ObjectHolder;

public class WailaModCompat implements IModCompat {

    @ObjectHolder("vampirism:garlic_beacon")
    static Block garlicBeacon;

    @Override
    public String getModID() {
        return "waila";
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {

    }


    @Override
    public void onInitStep(Step step, ModLifecycleEvent event) {

    }
}
