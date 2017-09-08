package de.teamlapen.vampirism_integrations.bop;

import biomesoplenty.api.biome.BOPBiomes;
import de.teamlapen.vampirism.api.VampirismAPI;

class Biomes {
    static void registerNoSundamageBiomes(BOPCompat compat) {
        if (compat.disabled_sundamage_ominous_woods && BOPBiomes.ominous_woods.isPresent()) {
            VampirismAPI.sundamageRegistry().addNoSundamageBiome(BOPBiomes.ominous_woods.get().getBiomeClass());
        }
    }
}