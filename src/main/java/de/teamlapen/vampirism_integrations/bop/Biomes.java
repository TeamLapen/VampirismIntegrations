package de.teamlapen.vampirism_integrations.bop;

import de.teamlapen.vampirism.api.VampirismAPI;
import net.minecraft.resources.ResourceLocation;

class Biomes {
    static void registerNoSundamageBiomes(BOPCompat compat) {
        if (compat.disabled_sundamage_ominous_woods.get()) {
            VampirismAPI.sundamageRegistry().addNoSundamageBiomes(new ResourceLocation("biomesoplenty:ominous_woods"));
        }
    }
}