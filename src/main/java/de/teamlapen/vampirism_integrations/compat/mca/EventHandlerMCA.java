package de.teamlapen.vampirism_integrations.compat.mca;

import de.teamlapen.vampirism.api.event.VampirismVillageEvent;
import de.teamlapen.vampirism_integrations.VampirismIntegrationsMod;
import mca.entity.EntityVillagerMCA;
import mca.enums.EnumProfession;
import mca.enums.EnumProfessionSkinGroup;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

class EventHandlerMCA {

    @SubscribeEvent
    public void onCreateAggressiveVillager(VampirismVillageEvent.MakeAggressive event) {
        try {
            if (event.getOldVillager() instanceof EntityVillagerMCA) {
                EntityAngryVillagerMCA angry = EntityAngryVillagerMCA.makeAngry((EntityVillagerMCA) event.getOldVillager());
                event.setAgressiveVillager(angry);
                event.setCanceled(true);
            }
        } catch (Exception e) {
            VampirismIntegrationsMod.log.e("MCA_Events", e, "Failed to make villager aggressive");
        }
    }

    @SubscribeEvent
    public void onSpawnNewVillager(VampirismVillageEvent.SpawnNewVillager event) {
        try {
            EntityVillagerMCA villager = new EntityVillagerMCA(event.getSeedVillager().getEntityWorld());
            villager.attributes.assignRandomName();
            villager.attributes.assignRandomGender();
            villager.attributes.assignRandomPersonality();
            if (event.isWillBeAggressive()) {
                villager.attributes.setProfession(EnumProfession.Guard);
                EnumProfession prof;
                do {
                    prof = EnumProfession.getAtRandom();
                } while (prof.getSkinGroup() == EnumProfessionSkinGroup.Guard);

                villager.attributes.setProfession(prof);
            } else {
                villager.attributes.assignRandomProfession();
            }
            villager.attributes.assignRandomSkin();
            villager.copyLocationAndAnglesFrom(event.getSeedVillager());
            event.setNewVillager(villager);
            event.setResult(Event.Result.ALLOW);
        } catch (Exception e) {
            VampirismIntegrationsMod.log.e("MCA_Events", e, "Failed to spawn new MCA villager");
        }
    }
}
