package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.lib.lib.util.IModCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class MCACompat implements IModCompat {

    public static final String CONVERTED_MALE_VILLAGER_ID = "male_villager_mca_converted";
    public static final String CONVERTED_FEMALE_VILLAGER_ID = "female_villager_mca_converted";
    public static final String ANGRY_MALE_VILLAGER_ID = "male_villager_mca_angry";
    public static final String ANGRY_FEMALE_VILLAGER_ID = "female_villager_mca_angry";

    public static final String ID = "mca";
    protected static final ResourceLocation MALE_VILLAGER = new ResourceLocation(ID, "male_villager");
    protected static final ResourceLocation FEMALE_VILLAGER = new ResourceLocation(ID, "female_villager");

    public MCACompat() {
        MCARegistration.registerEntities(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {

    }

    @Override
    public String getModID() {
        return ID;
    }

    @Override
    public void onInitStep(Step step, ParallelDispatchEvent event) {
        if (step == Step.COMMON_SETUP) {
            MinecraftForge.EVENT_BUS.register(new MCAEventHandler());
            MCARegistration.registerConvertibles();
        }
    }
}
