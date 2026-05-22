package de.teamlapen.vampirism_integrations.tan;

import de.teamlapen.vampirism.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Client side only
 */
public class OverlayHandler {

    private static final ResourceLocation THIRST_OVERLAY = new ResourceLocation("toughasnails", "thirst_level");

    @SubscribeEvent
    public void renderThirstLevel(RenderGuiOverlayEvent event) {
        if (event.getOverlay().id().equals(THIRST_OVERLAY) && TANCompat.disableThirst.get() &&  Minecraft.getInstance().player != null && Helper.isVampire(Minecraft.getInstance().player)) {
            event.setCanceled(true);
        }
    }
}
