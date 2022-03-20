package de.teamlapen.vampirism_integrations.bop;

import biomesoplenty.api.biome.BOPBiomes;
import de.teamlapen.vampirism.api.VampirismAPI;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;

class Biomes {
    private static final ResourceLocation hunter_camp_id = new ResourceLocation("vampirism", "hunter_camp");
    static void registerNoSundamageBiomes(BOPCompat compat) {
        if (compat.disabled_sundamage_ominous_woods.get()) {
            VampirismAPI.sundamageRegistry().addNoSundamageBiomes(BOPBiomes.ominous_woods.location());
            VampirismAPI.worldGenRegistry().removeStructureFromBiomes(hunter_camp_id, Arrays.asList(BOPBiomes.ominous_woods.location(), BOPBiomes.withered_abyss.location()));
        }
    }
}