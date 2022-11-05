package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.entity.convertible.IConvertedCreature;
import de.teamlapen.vampirism.api.event.VampirismVillageEvent;
import forge.net.mca.entity.VillagerEntityMCA;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class MCAEventHandler {


    @SubscribeEvent
    public void onCreateAggressiveVillager(VampirismVillageEvent.MakeAggressive event) {
        if (event.getOldVillager() instanceof VillagerEntityMCA villagerEntityMCA) {
            Villager v = AggressiveVillagerEntityMCA.makeAngry(villagerEntityMCA);
            if (v != null) {
                UtilLib.replaceEntity(event.getOldVillager(), v);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onSpawnNewVillager(VampirismVillageEvent.SpawnNewVillager event) {
        boolean gender = event.getNewVillager().getRandom().nextBoolean();
        boolean vampire = event.getNewVillager() instanceof IConvertedCreature<?>;

        EntityType<?> t = vampire ?  (gender ? MCARegistration.MALE_CONVERTED_VILLAGER.get() : MCARegistration.FEMALE_CONVERTED_VILLAGER.get()) : gender ? ForgeRegistries.ENTITY_TYPES.getValue(MCACompat.MALE_VILLAGER) : ForgeRegistries.ENTITY_TYPES.getValue(MCACompat.FEMALE_VILLAGER);
        event.setNewVillager((Villager) t.create(event.getWorld()));
    }
}
