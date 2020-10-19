package de.teamlapen.vampirism_integrations.bop;

import biomesoplenty.api.biome.BOPBiomes;
import de.teamlapen.vampirism.api.VampirismAPI;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Collections;

class Biomes {
    private static final ResourceLocation hunter_camp_id = new ResourceLocation("vampirism", "hunter_camp");
    static void registerNoSundamageBiomes(BOPCompat compat) {
        if (compat.disabled_sundamage_ominous_woods.get() && BOPBiomes.ominous_woods.isPresent()) {
            BOPBiomes.ominous_woods.ifPresent(b -> {
                VampirismAPI.sundamageRegistry().addNoSundamageBiome(b.getRegistryName());
                VampirismAPI.worldGenRegistry().removeStructureFromBiomes(hunter_camp_id, Collections.singletonList(b));
            } );
        }
    }
}