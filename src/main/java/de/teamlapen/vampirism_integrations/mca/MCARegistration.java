package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.convertible.IConvertingHandler;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import mca.entity.VillagerEntityMCA;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


public class MCARegistration {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, REFERENCE.MODID);


    public static final RegistryObject<EntityType<? extends Villager>> MALE_CONVERTED_VILLAGER = MCARegistration.prepareEntityType(MCACompat.CONVERTED_MALE_VILLAGER_ID, getBuilder(true), true);
    public static final RegistryObject<EntityType<? extends Villager>> FEMALE_CONVERTED_VILLAGER = MCARegistration.prepareEntityType(MCACompat.CONVERTED_FEMALE_VILLAGER_ID, getBuilder(false), false);
    public static final RegistryObject<EntityType<? extends Villager>> MALE_AGGRESSIVE_VILLAGER = MCARegistration.prepareEntityType(MCACompat.ANGRY_MALE_VILLAGER_ID, getBuilder(true), true);
    public static final RegistryObject<EntityType<? extends Villager>> FEMALE_AGGRESSIVE_VILLAGER = MCARegistration.prepareEntityType(MCACompat.ANGRY_FEMALE_VILLAGER_ID, getBuilder(true), true);


    static void registerEntities(IEventBus bus) {
        ENTITY_TYPES.register(bus);
        bus.addListener(MCARegistration::onRegisterEntityTypeAttributes);

    }

    private static Supplier<EntityType.Builder<? extends Villager>> getBuilder(boolean male) {
        return () -> {
            if (ModList.get().isLoaded(MCACompat.ID)) {
                return MCAEntityClassRedirect.create(male);
            } else {
                return EntityType.Builder.of(Villager::new, MobCategory.CREATURE);
            }
        };
    }

    private static RegistryObject<EntityType<? extends Villager>> prepareEntityType(String id, Supplier<EntityType.Builder<? extends Villager>> builder, boolean spawnable) {
        return ENTITY_TYPES.register(id, () -> {
            EntityType.Builder<? extends Villager> type = builder.get().setTrackingRange(80).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true);
            if (!spawnable)
                type.noSummon();
            return type.build(de.teamlapen.vampirism_integrations.util.REFERENCE.MODID + ":" + id);
        });
    }

    static void registerConvertibles() {
        ResourceLocation overlay = new ResourceLocation(REFERENCE.MODID, "mca/overlay.png");
        IConvertingHandler<?> c = new ConvertedVillagerEntityMCA.ConvertingHandler();
        VampirismAPI.entityRegistry().addConvertible((EntityType<? extends PathfinderMob>) ForgeRegistries.ENTITIES.getValue(MCACompat.MALE_VILLAGER), overlay, c);
        VampirismAPI.entityRegistry().addConvertible((EntityType<? extends PathfinderMob>) ForgeRegistries.ENTITIES.getValue(MCACompat.FEMALE_VILLAGER), overlay, c);
    }

    static void onRegisterEntityTypeAttributes(EntityAttributeCreationEvent event) {
        event.put(MALE_AGGRESSIVE_VILLAGER.get(), VillagerEntityMCA.createVillagerAttributes().build());
        event.put(FEMALE_AGGRESSIVE_VILLAGER.get(), VillagerEntityMCA.createVillagerAttributes().build());
        event.put(MALE_CONVERTED_VILLAGER.get(), VillagerEntityMCA.createVillagerAttributes().build());
        event.put(FEMALE_CONVERTED_VILLAGER.get(), VillagerEntityMCA.createVillagerAttributes().build());

    }
}
