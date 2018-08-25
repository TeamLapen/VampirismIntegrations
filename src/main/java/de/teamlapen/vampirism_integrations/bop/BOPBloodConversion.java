package de.teamlapen.vampirism_integrations.bop;


import de.teamlapen.vampirism.api.general.BloodConversionRegistry;
import de.teamlapen.vampirism_integrations.VampirismIntegrationsMod;
import net.minecraftforge.fluids.FluidRegistry;

public class BOPBloodConversion {
    static void registerBloodConversion() {
        if (!FluidRegistry.isFluidRegistered("hell_blood")) {
            VampirismIntegrationsMod.log.w("BOP", "Cannot find hell_blood fluid");
        } else {
            BloodConversionRegistry.registerFluidConversionRatio("hell_blood", BOPCompat.conversion_factor);
        }
    }
}
