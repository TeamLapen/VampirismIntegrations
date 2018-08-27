package de.teamlapen.vampirism_integrations.tan;

import de.teamlapen.vampirism_integrations.VampirismIntegrationsMod;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import toughasnails.api.TANPotions;

public class TANRegistration {

    static void registerPotions() {
        if (TANCompat.replace_hyperthermia) {
            if (TANPotions.hyperthermia == null) {
                VampirismIntegrationsMod.log.w("ToughAsNails", "Cannot find hyperthermia potion");
            } else {
                VampirismIntegrationsMod.log.i("ToughAsNails", "Substituting Hyperthermia potion");
                Potion hyperthermia = new PotionHyperthermiaVampirism(25);
                hyperthermia.setPotionName(TANPotions.hyperthermia.getName());
                hyperthermia.setRegistryName(TANPotions.hyperthermia.getRegistryName());
                ForgeRegistries.POTIONS.register(hyperthermia);
                TANPotions.hyperthermia = hyperthermia;//Replace TAN reference
            }
        }

        if (TANCompat.replace_hypothermia) {
            if (TANPotions.hypothermia == null) {
                VampirismIntegrationsMod.log.w("ToughAsNails", "Cannot find hypothermia potion");
            } else {
                VampirismIntegrationsMod.log.i("ToughAsNails", "Substituting Hypothermia potion");
                Potion hypothermia = new PotionHypothermiaVampirism(25);
                hypothermia.setPotionName(TANPotions.hypothermia.getName());
                hypothermia.setRegistryName(TANPotions.hypothermia.getRegistryName());
                ForgeRegistries.POTIONS.register(hypothermia);
                TANPotions.hyperthermia = hypothermia;//Replace TAN reference
            }
        }


    }
}
