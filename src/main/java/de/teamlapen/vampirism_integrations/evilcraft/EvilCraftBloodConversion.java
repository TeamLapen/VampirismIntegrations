package de.teamlapen.vampirism_integrations.evilcraft;

import de.teamlapen.vampirism.api.general.BloodConversionRegistry;
import de.teamlapen.vampirism_integrations.VampirismIntegrationsMod;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

class EvilCraftBloodConversion {

    @GameRegistry.ObjectHolder("evilcraft:blood_orb")
    private static Item blood_orb;


    static void registerBloodConversion() {
        VampirismIntegrationsMod.log.d("Evilcraft", "Registering blood conversion");
        if (!FluidRegistry.isFluidRegistered("evilcraftblood")) {
            VampirismIntegrationsMod.log.w("Evilcraft", "Cannot find evilcraft blood");
        } else {
            BloodConversionRegistry.registerFluidConversionRatio("evilcraftblood", EvilCraftCompat.conversionFactor);
        }

        if (blood_orb == null) {
            VampirismIntegrationsMod.log.w("Evilcraft", "Cannot get blood orb item");
        } else {
            BloodConversionRegistry.registerItem(blood_orb, (int) (EvilCraftCompat.conversionFactor * 3500));
        }
    }
}
