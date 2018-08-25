package de.teamlapen.vampirism_integrations.tconstruct;

import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.general.BloodConversionRegistry;
import de.teamlapen.vampirism_integrations.VampirismIntegrationsMod;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

class TConstructBloodConversion {

    @GameRegistry.ObjectHolder("tconstruct:edible")
    private static Item edible = null;


    static void registerBloodConversion() {
        VampirismIntegrationsMod.log.d("TinkersConstruct", "Registering blood conversion");
        if (!FluidRegistry.isFluidRegistered("blood")) {
            VampirismIntegrationsMod.log.w("TConstruct", "Cannot retrieve tinkers construct blood");
        } else {
            BloodConversionRegistry.registerFluidConversionRatio("blood", TConstructCompat.conversion_factor);
        }
        if (edible == null) {
            VampirismIntegrationsMod.log.w("TConstruct", "Cannot retrieve tinkers edible item");
        } else {
            BloodConversionRegistry.registerItem(new ResourceLocation("tconstruct", "edible"), stack -> stack.getMetadata() == 33 ? VReference.FOOD_TO_FLUID_BLOOD : 0);
        }

    }
}
