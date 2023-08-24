package de.teamlapen.vampirism_integrations.bop;

import biomesoplenty.api.biome.BOPBiomes;
import de.teamlapen.vampirism.api.VampirismAPI;

public class Biomes {
    static void registerNoSundamageBiomes(BOPCompat compat) {
        if (compat.disabled_sundamage_ominous_woods.get()) {
           VampirismAPI.sundamageRegistry().addNoSundamageBiomes(BOPBiomes.OMINOUS_WOODS.location());
        }
    }
}