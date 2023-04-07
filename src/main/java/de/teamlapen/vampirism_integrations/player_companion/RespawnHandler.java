package de.teamlapen.vampirism_integrations.player_companion;

import de.markusbordihn.playercompanions.entity.PlayerCompanionEntity;
import de.teamlapen.vampirism.entity.ExtendedCreature;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

class RespawnHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void onDeath(LivingDeathEvent event) {
        if (PlayerCompanionCompat.replenish_blood_on_death.get() && event.getEntity() instanceof PlayerCompanionEntity) {
            ExtendedCreature.getSafe(event.getEntity()).ifPresent(extended -> {
                extended.setBlood(extended.getMaxBlood());
            });
        }
    }


}
