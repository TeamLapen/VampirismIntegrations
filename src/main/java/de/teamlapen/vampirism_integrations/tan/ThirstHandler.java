package de.teamlapen.vampirism_integrations.tan;

import de.teamlapen.vampirism_integrations.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;

/**
 * Remove/Limit thirst for vampires
 */
public class ThirstHandler {

    private static final String ID = "toughasnails";
    private static final ResourceLocation THIRST_OVERLAY = ResourceLocation.fromNamespaceAndPath(ID, "thirst_level");

    @SubscribeEvent
    public void onPlayerUpdate(EntityTickEvent.Post event) {
        Entity e = event.getEntity();
        if (TANCompat.disableThirst.get() && e.tickCount % 32 == 0 && e instanceof Player && Helper.isVampire((Player) e)) {
            IThirst thirst = ThirstHelper.getThirst((Player) e);
            if (thirst.getThirst() < 10) {
                thirst.setThirst(10);
            }
        }
    }

    @SubscribeEvent
    public void renderThirstLevel(RenderGuiLayerEvent.Pre event) {
        if (event.getName().equals(THIRST_OVERLAY) && TANCompat.disableThirst.get() &&  Minecraft.getInstance().player != null && Helper.isVampire(Minecraft.getInstance().player)) {
            event.setCanceled(true);
        }
    }
}
