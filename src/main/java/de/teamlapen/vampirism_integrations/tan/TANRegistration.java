package de.teamlapen.vampirism_integrations.tan;

import de.teamlapen.vampirism.world.gen.VampirismWorldGen;
import de.teamlapen.vampirism_integrations.VampirismIntegrationsMod;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import toughasnails.api.TANBlocks;
import toughasnails.api.TANPotions;
import toughasnails.block.BlockTANCampfire;

import java.util.Set;

public class TANRegistration {

    static void registerPotions() {

        try {
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
                    TANPotions.hypothermia = hypothermia;//Replace TAN reference
                }
            }
        } catch (Exception e) {
            VampirismIntegrationsMod.log.e("TAN", e, "Failed to create/register modified potions");
        }
    }

    static void registerCampfire() {
        //TODO access directly once next Vampirism version is released
        try {
            Set<IWorldGenerator> set = ReflectionHelper.getPrivateValue(GameRegistry.class, null, "worldGenerators");
            for (IWorldGenerator gen : set) {
                if (gen instanceof VampirismWorldGen) {
                    ((VampirismWorldGen) gen).hunterCamp.setCampfireBlockstate(TANBlocks.campfire.getDefaultState().withProperty(BlockTANCampfire.BURNING, true));
                    return;
                }
            }
            throw new IllegalStateException("Did not find VampirismWorldGen");
        } catch (Exception e) {
            VampirismIntegrationsMod.log.e("TAN", e, "Failed to find Vampirism access transformer");
        }

    }
}
