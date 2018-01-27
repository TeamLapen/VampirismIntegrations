package de.teamlapen.vampirism_integrations.compat.mca;

import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MCACompatREFERENCE {

    @GameRegistry.ObjectHolder("vampirism:player.bite")
    static final SoundEvent player_bite = null;

    @GameRegistry.ObjectHolder("vampirism:pitchfork")
    static final Item pitchfork = null;

    /**
     * Check for vampire garlic damage every n ticks
     * Must be higher than 1, due to implementation
     */
    final static int REFRESH_GARLIC_TICKS = 40;
    /**
     * Check for vampire sun damage every n ticks
     * Must be higher than 2 due to implementation
     */
    final static int REFRESH_SUNDAMAGE_TICKS = 8;
}
