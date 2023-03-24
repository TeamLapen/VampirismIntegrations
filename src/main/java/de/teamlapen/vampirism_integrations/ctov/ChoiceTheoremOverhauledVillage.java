package de.teamlapen.vampirism_integrations.ctov;

import de.teamlapen.vampirism.util.MixinHooks;
import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;

import java.util.Arrays;

/**
 * It is currently required to register single jigsaw pieces by code to generate them only once per village. This is subject to change, thus the extra `vampirism/single_jigsaw_pieces.json` file.
 */
public class ChoiceTheoremOverhauledVillage implements IModCompat {
    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {

    }

    @Override
    public String getModID() {
        return "ctov";
    }

    @Override
    public void onInitStep(Step step, ParallelDispatchEvent event) {
        if (step == Step.ENQUEUE_IMC) {
            registerHunterHouses();
        }
    }

    private void registerHunterHouses() {
        registerHunterHouses("beach", "christmas", "desert", "desert_oasis", "halloween", "jungle", "jungle_tree", "mesa", "mesa_fortified", "mountain", "mountain_alpine", "mushroom", "plains", "plains_fortified", "savanna", "savanna_na", "snowy_igloo", "swamp", "swamp_fortified", "taiga", "taiga_fortified");
    }
    private void registerHunterHouses(String... paths) {
        String template = "village/%s/jobsite/hunter_trainer";
        MixinHooks.addSingleInstanceStructure(Arrays.stream(paths).map(path -> new ResourceLocation("ctov", template.formatted(path))).toList());
    }
}
