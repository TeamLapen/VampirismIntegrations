package de.teamlapen.vampirism_integrations.tan;

import de.teamlapen.vampirism.util.Helper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;

/**
 * Remove/Limit thirst for vampires
 */
public class ThirstHandler {
    @SubscribeEvent
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        Entity e = event.getEntity();
        if (TANCompat.disableThirst.get() && e.ticksExisted % 32 == 0 && e instanceof PlayerEntity && Helper.isVampire((PlayerEntity) e)) {
            IThirst thirst = ThirstHelper.getThirst((PlayerEntity) e);
            if (thirst.getThirst() < 2) {
                thirst.setThirst(2);
            }
        }
    }
}
