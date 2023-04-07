package de.teamlapen.vampirism_integrations.player_companion;

import de.teamlapen.lib.lib.util.IInitListener;
import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;

public class PlayerCompanionCompat implements IModCompat {

    static ForgeConfigSpec.BooleanValue replenish_blood_on_death;

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        replenish_blood_on_death = builder.comment("Replenish companion blood on companion death").define("replenish_blood_on_death", true);
    }

    @Override
    public String getModID() {
        return "player_companions";
    }

    @Override
    public void onInitStep(IInitListener.Step step, ParallelDispatchEvent event) {
        if (step == IInitListener.Step.COMMON_SETUP) {
            MinecraftForge.EVENT_BUS.addListener(RespawnHandler::onDeath);
        }
    }
}
