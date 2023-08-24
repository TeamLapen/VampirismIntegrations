package de.teamlapen.vampirism_integrations.graveyard;


import de.teamlapen.vampirism.api.VampirismAPI;
import net.finallion.nyctophobia.init.NBiomes;

public class Biomes {

    static void registerNoSundamageBiomes(GraveyardCompat compat) {
        if (compat.disabled_sundamage_haunted.get()) {
            VampirismAPI.sundamageRegistry().addNoSundamageBiomes(NBiomes.ERODED_HAUNTED_FOREST_KEY.location(), NBiomes.HAUNTED_FOREST_KEY.location(), NBiomes.HAUNTED_LAKES_KEY.location());
        }
        if (compat.disabled_sundamage_other.get()) {
            VampirismAPI.sundamageRegistry().addNoSundamageBiomes(NBiomes.DEEP_DARK_FOREST_KEY.location(), NBiomes.ANCIENT_DEAD_CORAL_REEF_KEY.location());
        }
    }
}
