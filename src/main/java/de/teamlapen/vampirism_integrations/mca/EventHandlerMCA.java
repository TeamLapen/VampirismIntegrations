package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.vampirism.api.event.VampirismVillageEvent;
import de.teamlapen.vampirism_integrations.VampirismIntegrationsMod;
import mca.entity.EntityVillagerMCA;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

class EventHandlerMCA {

    @SubscribeEvent
    public void onCreateAggressiveVillager(VampirismVillageEvent.MakeAggressive event) {
        if (event.getOldVillager() instanceof EntityVillagerMCA) {
            EntityAngryVillagerMCA angry = EntityAngryVillagerMCA.makeAngry((EntityVillagerMCA) event.getOldVillager());
            event.setAgressiveVillager(angry);
            event.setCanceled(true);
            VampirismIntegrationsMod.log.t("Canceld");
        }
    }
}
