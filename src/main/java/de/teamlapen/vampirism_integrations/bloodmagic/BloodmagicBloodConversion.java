package de.teamlapen.vampirism_integrations.bloodmagic;

import de.teamlapen.vampirism.api.general.BloodConversionRegistry;
import de.teamlapen.vampirism_integrations.VampirismIntegrationsMod;
import net.minecraftforge.fluids.FluidRegistry;

class BloodmagicBloodConversion {

    static void registerBloodConversion() {
        if (!FluidRegistry.isFluidRegistered("lifeessence")) {
            VampirismIntegrationsMod.log.w("Bloodmagic", "Cannot find life essence fluid");
        } else {
            BloodConversionRegistry.registerFluidConversionRatio("lifeessence", 1.0f);
        }
    }
}
