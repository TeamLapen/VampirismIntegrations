package de.teamlapen.vampirism_integrations;

import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.InvalidVersionSpecificationException;
import net.minecraftforge.fml.common.versioning.VersionRange;

import javax.annotation.Nullable;

/**
 * Dummy integration for Vampirism itself
 */
public class VampirismCompat implements IModCompat {

    public static boolean disableVersionCheck;

    @Override
    public String getModID() {
        return REFERENCE.VAMPIRISM_ID;
    }

    @Nullable
    @Override
    public String getAcceptedVersionRange() {
        return "[1.5.0,)";
    }

    @Override
    public void loadConfigs(Configuration config, ConfigCategory category) {
        disableVersionCheck = config.getBoolean("disable_VersionCheck", category.getName(), false, "Disable to automatic version check");
    }

    @Override
    public void onInitStep(Step step, FMLStateEvent event) {
        if (step == Step.POST_INIT) {
            FluidRegistry.getFluid(VReference.FLUID_BLOOD_NAME).setUnlocalizedName("vampirismintegrations.blood");
        }
    }


}
