package de.teamlapen.vampirism_integrations.tan;

import de.teamlapen.vampirism_integrations.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

/**
 * Client only
 */
public class OverlayHandler {

    private static final String ID = "toughasnails";
    private static final ResourceLocation THIRST_OVERLAY = ResourceLocation.fromNamespaceAndPath(ID, "thirst_level");

    @SubscribeEvent
    public void renderThirstLevel(RenderGuiLayerEvent.Pre event) {
        if (event.getName().equals(THIRST_OVERLAY) && TANCompat.disableThirst.get() &&  Minecraft.getInstance().player != null && Helper.isVampire(Minecraft.getInstance().player)) {
            event.setCanceled(true);
        }
    }
}
