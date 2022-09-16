package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.convertible.IConvertingHandler;
import de.teamlapen.vampirism_integrations.mca.client.ClientProxy;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import forge.net.mca.entity.VillagerEntityMCA;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;


public class MCARegistration {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, REFERENCE.MODID);


    public static final RegistryObject<EntityType<ConvertedVillagerEntityMCA>> MALE_CONVERTED_VILLAGER = MCARegistration.prepareEntityType(MCACompat.CONVERTED_MALE_VILLAGER_ID, getBuilder(MCAEntityClassRedirect::createConverted,true), true);
    public static final RegistryObject<EntityType<ConvertedVillagerEntityMCA>> FEMALE_CONVERTED_VILLAGER = MCARegistration.prepareEntityType(MCACompat.CONVERTED_FEMALE_VILLAGER_ID, getBuilder(MCAEntityClassRedirect::createConverted,false), false);
    public static final RegistryObject<EntityType<AngryVillagerEntityMCA>> MALE_AGGRESSIVE_VILLAGER = MCARegistration.prepareEntityType(MCACompat.ANGRY_MALE_VILLAGER_ID, getBuilder(MCAEntityClassRedirect::createAngry,true), true);
    public static final RegistryObject<EntityType<AngryVillagerEntityMCA>> FEMALE_AGGRESSIVE_VILLAGER = MCARegistration.prepareEntityType(MCACompat.ANGRY_FEMALE_VILLAGER_ID, getBuilder(MCAEntityClassRedirect::createAngry,false), true);


    static void registerEntities(IEventBus bus) {
        ENTITY_TYPES.register(bus);
        bus.addListener(MCARegistration::onRegisterEntityTypeAttributes);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> bus.addListener(ClientProxy::onRegisterRenderer));
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

    private static <T extends Villager> RegistryObject<EntityType<T>> prepareEntityType(String id, @Nullable Supplier<EntityType.Builder<T>> builder, boolean spawnable) {
        if (ModList.get().isLoaded(MCACompat.ID) && builder != null) {
            return ENTITY_TYPES.register(id, () -> {
                EntityType.Builder<T> type = builder.get().setTrackingRange(80).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true);
                if (!spawnable)
                    type.noSummon();
                return type.build(de.teamlapen.vampirism_integrations.util.REFERENCE.MODID + ":" + id);
            });
        } else {
            return RegistryObject.createOptional(new ResourceLocation(REFERENCE.MODID, id),ForgeRegistries.ENTITY_TYPES.getRegistryName(),REFERENCE.MODID);
        }

    }

    static void registerConvertibles() {
        ResourceLocation overlay = new ResourceLocation(REFERENCE.MODID, "mca/overlay.png");
        IConvertingHandler<?> c = new ConvertedVillagerEntityMCA.ConvertingHandler();
        VampirismAPI.entityRegistry().addConvertible((EntityType<? extends PathfinderMob>) ForgeRegistries.ENTITY_TYPES.getValue(MCACompat.MALE_VILLAGER), overlay, c);
        VampirismAPI.entityRegistry().addConvertible((EntityType<? extends PathfinderMob>) ForgeRegistries.ENTITY_TYPES.getValue(MCACompat.FEMALE_VILLAGER), overlay, c);
    }

    static void onRegisterEntityTypeAttributes(EntityAttributeCreationEvent event) {
        MALE_AGGRESSIVE_VILLAGER.ifPresent(entry -> event.put(entry, VillagerEntityMCA.createVillagerAttributes().build()));
        FEMALE_AGGRESSIVE_VILLAGER.ifPresent(entry -> event.put(entry, VillagerEntityMCA.createVillagerAttributes().build()));
        MALE_CONVERTED_VILLAGER.ifPresent(entry -> event.put(entry, VillagerEntityMCA.createVillagerAttributes().build()));
        FEMALE_CONVERTED_VILLAGER.ifPresent(entry -> event.put(entry, VillagerEntityMCA.createVillagerAttributes().build()));
    }
}
