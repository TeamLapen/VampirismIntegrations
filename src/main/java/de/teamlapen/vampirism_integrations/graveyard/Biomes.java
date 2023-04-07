package de.teamlapen.vampirism_integrations.graveyard;


import de.teamlapen.vampirism.api.VampirismAPI;
import net.finallion.graveyard_biomes.init.TGBiomes;

public class Biomes {

    static void registerNoSundamageBiomes(GraveyardCompat compat) {
        if (compat.disabled_sundamage_haunted.get()) {
            VampirismAPI.sundamageRegistry().addNoSundamageBiomes(TGBiomes.ERODED_HAUNTED_FOREST_KEY.location(), TGBiomes.HAUNTED_FOREST_KEY.location(), TGBiomes.HAUNTED_LAKES_KEY.location());
        }
        if (compat.disabled_sundamage_other.get()) {
            VampirismAPI.sundamageRegistry().addNoSundamageBiomes(TGBiomes.DEEP_DARK_FOREST_KEY.location(), TGBiomes.ANCIENT_DEAD_CORAL_REEF_KEY.location());
        }
    }
}
