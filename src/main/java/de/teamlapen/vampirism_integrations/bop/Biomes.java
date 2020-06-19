package de.teamlapen.vampirism_integrations.bop;

import biomesoplenty.api.biome.BOPBiomes;
import de.teamlapen.vampirism.api.VampirismAPI;

class Biomes {
    static void registerNoSundamageBiomes(BOPCompat compat) {
        if (compat.disabled_sundamage_ominous_woods.get() && BOPBiomes.ominous_woods.isPresent()) {
            BOPBiomes.ominous_woods.ifPresent(b -> VampirismAPI.sundamageRegistry().addNoSundamageBiome(b.getRegistryName()));
        }
    }
}