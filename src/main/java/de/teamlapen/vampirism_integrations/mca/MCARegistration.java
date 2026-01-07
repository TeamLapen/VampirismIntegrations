package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.convertible.IConvertingHandler;
import de.teamlapen.vampirism_integrations.mca.client.ClientRegistrationProxy;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import net.conczin.mca.entity.VillagerEntityMCA;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;


public class MCARegistration {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, REFERENCE.MODID);
    public static final DeferredHolder<EntityType<?>,EntityType<ConvertedVillagerEntityMCA>> MALE_CONVERTED_VILLAGER = MCARegistration.prepareEntityType(MCACompat.CONVERTED_MALE_VILLAGER_ID, getBuilder(MCAEntityClassRedirect::createConverted, true), true);
    public static final DeferredHolder<EntityType<?>,EntityType<ConvertedVillagerEntityMCA>> FEMALE_CONVERTED_VILLAGER = MCARegistration.prepareEntityType(MCACompat.CONVERTED_FEMALE_VILLAGER_ID, getBuilder(MCAEntityClassRedirect::createConverted, false), false);
    public static final DeferredHolder<EntityType<?>,EntityType<AggressiveVillagerEntityMCA>> MALE_AGGRESSIVE_VILLAGER = MCARegistration.prepareEntityType(MCACompat.ANGRY_MALE_VILLAGER_ID, getBuilder(MCAEntityClassRedirect::createAngry, true), true);
    public static final DeferredHolder<EntityType<?>,EntityType<AggressiveVillagerEntityMCA>> FEMALE_AGGRESSIVE_VILLAGER = MCARegistration.prepareEntityType(MCACompat.ANGRY_FEMALE_VILLAGER_ID, getBuilder(MCAEntityClassRedirect::createAngry, false), true);


    static void registerEntities(IEventBus bus) {
        ENTITY_TYPES.register(bus);
        bus.addListener(MCARegistration::onRegisterEntityTypeAttributes);
        if(FMLEnvironment.dist == Dist.CLIENT) {
            bus.addListener(ClientRegistrationProxy::onRegisterRenderer);
        }
    }

    private static <T extends Villager> Supplier<EntityType.Builder<T>> getBuilder(Function<Boolean, EntityType.Builder<T>> supplier, boolean male) {
        return () -> {
            if (ModList.get().isLoaded(MCACompat.ID)) {
                return supplier.apply(male);
            } else {
                return null;
            }
        };
    }

    private static <T extends Villager> DeferredHolder<EntityType<?>,EntityType<T>> prepareEntityType(String id, @Nullable Supplier<EntityType.Builder<T>> builder, boolean spawnable) {
//        if (ModList.get().isLoaded(MCACompat.ID) && builder != null) { TODO test when running without MCA
            return ENTITY_TYPES.register(id, () -> {
                EntityType.Builder<T> type = builder.get().setTrackingRange(80).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true);
                if (!spawnable)
                    type.noSummon();
                return type.build(de.teamlapen.vampirism_integrations.util.REFERENCE.MODID + ":" + id);
            });
    }

    static void registerConvertibles() {
        ResourceLocation overlay = ResourceLocation.fromNamespaceAndPath(REFERENCE.MODID, "mca/overlay.png");
        IConvertingHandler<?> c = new ConvertedVillagerEntityMCA.ConvertingHandler();
        VampirismAPI.entityRegistry().addConvertible((EntityType<? extends PathfinderMob>) ForgeRegistries.ENTITY_TYPES.getValue(MCACompat.MALE_VILLAGER), overlay, c);
        VampirismAPI.entityRegistry().addConvertible((EntityType<? extends PathfinderMob>) ForgeRegistries.ENTITY_TYPES.getValue(MCACompat.FEMALE_VILLAGER), overlay, c);
    }

    static void onRegisterEntityTypeAttributes(EntityAttributeCreationEvent event) {
        event.put(MALE_AGGRESSIVE_VILLAGER.get(), VillagerEntityMCA.createAttributes().build());
        event.put(FEMALE_AGGRESSIVE_VILLAGER.get(), VillagerEntityMCA.createAttributes().build());
        event.put(MALE_CONVERTED_VILLAGER.get(), VillagerEntityMCA.createAttributes().build());
        event.put(FEMALE_CONVERTED_VILLAGER.get(), VillagerEntityMCA.createAttributes().build());
    }
}
