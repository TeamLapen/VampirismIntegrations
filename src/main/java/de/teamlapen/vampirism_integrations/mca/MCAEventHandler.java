package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.event.VampirismVillageEvent;
import mca.entity.VillagerEntityMCA;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class MCAEventHandler {


    @SubscribeEvent
    public void onCreateAggressiveVillager(VampirismVillageEvent.MakeAggressive event) {
        if (event.getOldVillager() instanceof VillagerEntityMCA villagerEntityMCA) {
            Villager v = AngryVillagerEntityMCA.makeAngry(villagerEntityMCA);
            if (v != null) {
                UtilLib.replaceEntity(event.getOldVillager(), v);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onSpawnNewVillager(VampirismVillageEvent.SpawnNewVillager event) {
        EntityType<?> t = event.getOldEntity() != null && event.getOldEntity().getRandom().nextBoolean() ? ForgeRegistries.ENTITIES.getValue(MCACompat.MALE_VILLAGER) : ForgeRegistries.ENTITIES.getValue(MCACompat.FEMALE_VILLAGER);
        event.setNewVillager((Villager) t.create(event.getWorld()));
    }
}
