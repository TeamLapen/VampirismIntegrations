package de.teamlapen.vampirism_integrations.mca;

import com.google.common.base.Optional;
import de.teamlapen.vampirism.api.event.VampirismVillageEvent;
import de.teamlapen.vampirism_integrations.VampirismIntegrationsMod;
import mca.entity.EntityVillagerMCA;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

class EventHandlerMCA {

    @SubscribeEvent
    public void onCreateAggressiveVillager(VampirismVillageEvent.MakeAggressive event) {
        try {
            if (event.getOldVillager() instanceof EntityVillagerMCA) {
                EntityAngryVillagerMCA angry = EntityAngryVillagerMCA.makeAngry((EntityVillagerMCA) event.getOldVillager());
                event.setAgressiveVillager(angry); //Can be null to prevent conversion
                event.setCanceled(true);
            }
        } catch (Exception e) {
            VampirismIntegrationsMod.log.e("MCA_Events", e, "Failed to make villager aggressive");
        }
    }

    @SubscribeEvent
    public void onSpawnNewVillager(VampirismVillageEvent.SpawnNewVillager event) {
        try {
            EntityVillagerMCA villager = new EntityVillagerMCA(event.getSeedVillager().getEntityWorld(), Optional.absent(), Optional.absent());
            villager.copyLocationAndAnglesFrom(event.getSeedVillager());
            event.setNewVillager(villager);
            event.setResult(Event.Result.ALLOW);
        } catch (Exception e) {
            VampirismIntegrationsMod.log.e("MCA_Events", e, "Failed to spawn new MCA villager");
        }
    }


}
