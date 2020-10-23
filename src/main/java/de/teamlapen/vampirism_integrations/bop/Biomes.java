package de.teamlapen.vampirism_integrations.bop;

import biomesoplenty.api.biome.BOPBiomes;
import de.teamlapen.vampirism.api.VampirismAPI;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class Biomes {
    private static final ResourceLocation hunter_camp_id = new ResourceLocation("vampirism", "hunter_camp");
    static void registerNoSundamageBiomes(BOPCompat compat) {
        if (compat.disabled_sundamage_ominous_woods.get() && BOPBiomes.ominous_woods.isPresent()) {
            List<Biome> noTentBiomes = new ArrayList<>();
            BOPBiomes.ominous_woods.ifPresent(b -> {
                VampirismAPI.sundamageRegistry().addNoSundamageBiome(b.getRegistryName());
                noTentBiomes.add(b);
            } );
            BOPBiomes.outback.ifPresent(noTentBiomes::add);
            VampirismAPI.worldGenRegistry().removeStructureFromBiomes(hunter_camp_id, noTentBiomes);
        }
    }
}