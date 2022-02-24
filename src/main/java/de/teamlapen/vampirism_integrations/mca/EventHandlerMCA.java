package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.event.VampirismVillageEvent;
import de.teamlapen.vampirism_integrations.VampirismIntegrationsMod;
import mca.entity.VillagerEntityMCA;
import mca.entity.ai.relationship.Gender;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;


class EventHandlerMCA {

    @SubscribeEvent
    public void onCreateAggressiveVillager(VampirismVillageEvent.MakeAggressive event) {
        try {
            if (event.getOldVillager() instanceof VillagerEntityMCA) {
                EntityAngryVillagerMCA angry = EntityAngryVillagerMCA.makeAngry((VillagerEntityMCA) event.getOldVillager());
                event.setCanceled(true);
                UtilLib.replaceEntity(event.getOldVillager(), angry);
            }
        } catch (Exception e) {
            VampirismIntegrationsMod.LOGGER.error("Failed to make villager aggressive", e);
        }
    }

    @SubscribeEvent
    public void onSpawnNewVillager(VampirismVillageEvent.SpawnNewVillager event) {
        try {
            Gender g = Gender.getRandom();
            VillagerEntityMCA villager = new VillagerEntityMCA(g.getVillagerType(), event.getWorld(), g);
            if (event.getOldEntity() != null) {
                villager.copyPosition(event.getOldEntity());
            }
            event.setNewVillager(villager);
            event.setResult(Event.Result.ALLOW);
        } catch (Exception e) {
            VampirismIntegrationsMod.LOGGER.error("Failed to spawn new MCA villager", e);
        }
    }


}
