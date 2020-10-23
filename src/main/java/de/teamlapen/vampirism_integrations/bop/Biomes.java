package de.teamlapen.vampirism_integrations.bop;

import biomesoplenty.api.biome.BOPBiomes;
import de.teamlapen.vampirism.api.VampirismAPI;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

class Biomes {
    private static final ResourceLocation hunter_camp_id = new ResourceLocation("vampirism", "hunter_camp");
    static void registerNoSundamageBiomes(BOPCompat compat) {
        if (compat.disabled_sundamage_ominous_woods.get()) {
            VampirismAPI.sundamageRegistry().addNoSundamageBiomes(BOPBiomes.ominous_woods.getLocation());
            VampirismAPI.worldGenRegistry().removeStructureFromBiomes(hunter_camp_id, Arrays.asList(BOPBiomes.ominous_woods.getLocation(), BOPBiomes.withered_abyss.getLocation(), BOPBiomes.outback.getLocation()));
        }
    }
}