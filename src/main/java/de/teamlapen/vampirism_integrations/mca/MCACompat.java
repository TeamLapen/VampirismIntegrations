package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.lib.lib.util.IInitListener;
import de.teamlapen.lib.lib.util.IModCompat;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism_integrations.IInterModeEnqueue;
import de.teamlapen.vampirism_integrations.mca.client.ClientProxy;
import de.teamlapen.vampirism_integrations.mca.client.OverlayAssignmentLoader;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import mca.entity.EntitiesMCA;
import mca.entity.ai.relationship.Gender;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nullable;

public class MCACompat implements IModCompat, IInterModeEnqueue {


    public static final String ID = "mca";

    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        if (ModList.get().isLoaded(ID)) { //TODO is this a good idea?
            event.getRegistry().register(MCACompat.<EntityConvertedVillagerMCA>prepareEntity((type, level) -> new EntityConvertedVillagerMCA(type, level, Gender.MALE), "mca_converted_villager_male"));
            event.getRegistry().register(MCACompat.<EntityConvertedVillagerMCA>prepareEntity((type, level) -> new EntityConvertedVillagerMCA(type, level, Gender.FEMALE), "mca_converted_villager_female"));
            event.getRegistry().register(MCACompat.<EntityAngryVillagerMCA>prepareEntity((type, level) -> new EntityAngryVillagerMCA(type, level, Gender.MALE), "mca_angry_villager_male"));
            event.getRegistry().register(MCACompat.<EntityAngryVillagerMCA>prepareEntity((type, level) -> new EntityAngryVillagerMCA(type, level, Gender.FEMALE), "mca_angry_villager_female"));
        }
    }

    public static void registerEntityTypeAttributes(EntityAttributeCreationEvent event) {
        if (ModList.get().isLoaded(ID)) { //TODO is this a good idea?
            event.put(EntityAngryVillagerMCA.angry_villager_male, EntityAngryVillagerMCA.createVillagerAttributes().build());
            event.put(EntityAngryVillagerMCA.angry_villager_female, EntityAngryVillagerMCA.createVillagerAttributes().build());
            event.put(EntityConvertedVillagerMCA.converted_villager_male, EntityConvertedVillagerMCA.createVillagerAttributes().build());
            event.put(EntityConvertedVillagerMCA.converted_villager_female, EntityConvertedVillagerMCA.createVillagerAttributes().build());
        }
    }

    private static <T extends EntityVillagerVampirismMCA> EntityType<T> prepareEntity(EntityType.IFactory<T> factory, String id) {
        EntityType.Builder<T> builder = EntityType.Builder.of(factory, VReference.VAMPIRE_CREATURE_TYPE).sized(0.6F, 1.95F).setTrackingRange(80).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true);
        EntityType<T> entry = builder.build(REFERENCE.MODID + ":" + id);
        entry.setRegistryName(REFERENCE.MODID, id);
        return entry;
    }

    @Override
    public String getModID() {
        return ID;
    }

    public MCACompat() {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(EntityType.class, MCACompat::registerEntities);
        FMLJavaModLoadingContext.get().getModEventBus().addListener( MCACompat::registerEntityTypeAttributes);
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {

    }

    @Override
    public void enqueueIMC(InterModEnqueueEvent event) {
        VampirismAPI.entityRegistry().addConvertible(EntitiesMCA.FEMALE_VILLAGER, null, new EntityConvertedVillagerMCA.ConvertingHandler());
        VampirismAPI.entityRegistry().addConvertible(EntitiesMCA.MALE_VILLAGER, null, new EntityConvertedVillagerMCA.ConvertingHandler());
    }

    @Nullable
    @Override
    public String getAcceptedVersionRange() {
        return "[7.0.8,)";
    }

    @Override
    public void onInitStep(IInitListener.Step step, ParallelDispatchEvent event) {
        if (step == Step.CLIENT_SETUP) {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> //                OverlayAssignmentLoader.init((event).getModConfigurationDirectory());
                    //                OverlayAssignmentLoader.registerSaveCommand();
                    ClientProxy::registerRenderer);
        }
        if (step == Step.COMMON_SETUP) {
            MinecraftForge.EVENT_BUS.register(new EventHandlerMCA());
        }
    }
}
