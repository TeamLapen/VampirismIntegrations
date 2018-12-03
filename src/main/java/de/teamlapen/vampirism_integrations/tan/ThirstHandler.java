package de.teamlapen.vampirism_integrations.tan;

import de.teamlapen.vampirism.util.Helper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.stat.capability.IThirst;
import toughasnails.api.thirst.ThirstHelper;


public class ThirstHandler {

    @SubscribeEvent
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity().ticksExisted % 32 == 0 && event.getEntity() instanceof EntityPlayer && Helper.isVampire(event.getEntity())) {
            IThirst thirst = ThirstHelper.getThirstData((EntityPlayer) event.getEntity());
            if (thirst.getThirst() < 2) {
                thirst.setThirst(2);
            }
        }
    }
}
