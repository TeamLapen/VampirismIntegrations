package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.ParallelDispatchEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

public class MCACompat implements IModCompat {

    public static final String CONVERTED_MALE_VILLAGER_ID = "male_villager_mca_converted";
    public static final String CONVERTED_FEMALE_VILLAGER_ID = "female_villager_mca_converted";
    public static final String ANGRY_MALE_VILLAGER_ID = "male_villager_mca_angry";
    public static final String ANGRY_FEMALE_VILLAGER_ID = "female_villager_mca_angry";

    public static final String ID = "mca";
    protected static final ResourceLocation MALE_VILLAGER = ResourceLocation.fromNamespaceAndPath(ID, "male_villager");
    protected static final ResourceLocation FEMALE_VILLAGER = ResourceLocation.fromNamespaceAndPath(ID, "female_villager");

    public MCACompat(IEventBus bus) {
        MCARegistration.registerEntities(bus);
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {

    }

    @Nullable
    @Override
    public String getAcceptedVersionRange() {
        return "[7.3.19,)";
    }

    @Override
    public String getModID() {
        return ID;
    }

    @Override
    public void onInitStep(Step step, ParallelDispatchEvent event) {
        if (step == Step.COMMON_SETUP) {
            NeoForge.EVENT_BUS.register(new MCAEventHandler());
            MCARegistration.registerConvertibles();
        }
    }
}
