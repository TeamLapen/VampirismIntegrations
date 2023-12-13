package de.teamlapen.vampirism_integrations.tan;

import de.teamlapen.vampirism.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;

/**
 * Remove/Limit thirst for vampires
 */
public class ThirstHandler {

    private static final ResourceLocation THIRST_OVERLAY = new ResourceLocation("toughasnails", "thirst_level");
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

    @SubscribeEvent
    public void renderThirstLevel(RenderGuiOverlayEvent event) {
        if (event.getOverlay().id().equals(THIRST_OVERLAY) && TANCompat.disableThirst.get() &&  Minecraft.getInstance().player != null && Helper.isVampire(Minecraft.getInstance().player)) {
            event.setCanceled(true);
        }
    }
}
