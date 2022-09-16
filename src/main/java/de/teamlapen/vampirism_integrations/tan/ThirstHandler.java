package de.teamlapen.vampirism_integrations.tan;

import de.teamlapen.vampirism.util.Helper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;

/**
 * Remove/Limit thirst for vampires
 */
public class ThirstHandler {
    @SubscribeEvent
    public void onPlayerUpdate(LivingEvent.LivingTickEvent event) {
        Entity e = event.getEntity();
        if (TANCompat.disableThirst.get() && e.tickCount % 32 == 0 && e instanceof Player && Helper.isVampire((Player) e)) {
            IThirst thirst = ThirstHelper.getThirst((Player) e);
            if (thirst.getThirst() < 10) {
                thirst.setThirst(10);
            }
        }
    }
}
