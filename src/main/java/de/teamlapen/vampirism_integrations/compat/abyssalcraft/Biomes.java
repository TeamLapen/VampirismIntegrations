package de.teamlapen.vampirism_integrations.compat.abyssalcraft;

import com.shinoow.abyssalcraft.api.biome.IDarklandsBiome;
import de.teamlapen.vampirism.api.VampirismAPI;

class Biomes {

    static void registerNoSundamageBiomes(AbyssalcraftCompat compat) {
        if (compat.disableSundamage_darklands) {
            VampirismAPI.sundamageRegistry().addNoSundamageBiome(IDarklandsBiome.class);

        }
    }
}
