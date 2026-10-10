package de.teamlapen.vampirism_integrations.graveyard;


import com.lion.nyctophobia.init.NBiomes;
import de.teamlapen.vampirism.api.VampirismAPI;

public class Biomes {

    static void registerNoSundamageBiomes(GraveyardCompat compat) {
        if (compat.disabled_sundamage_haunted.get()) {
            VampirismAPI.sundamageRegistry().addNoSundamageBiomes(NBiomes.ERODED_HAUNTED_FOREST_KEY, NBiomes.HAUNTED_FOREST_KEY, NBiomes.HAUNTED_LAKES_KEY);
        }
        if (compat.disabled_sundamage_other.get()) {
            VampirismAPI.sundamageRegistry().addNoSundamageBiomes(NBiomes.DEEP_DARK_FOREST, NBiomes.ANCIENT_DEAD_CORAL_REEF_KEY);
        }
    }
}
