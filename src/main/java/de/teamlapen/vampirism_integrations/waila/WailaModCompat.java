package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.lib.lib.util.IInitListener;
import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.event.lifecycle.ParallelDispatchEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.registries.DeferredHolder;

public class WailaModCompat implements IModCompat {

    @Override
    public String getModID() {
        return "waila";
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {

    }


    @Override
    public void onInitStep(IInitListener.Step step, ParallelDispatchEvent event) {

    }
}
