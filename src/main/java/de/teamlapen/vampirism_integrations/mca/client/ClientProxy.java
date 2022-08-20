package de.teamlapen.vampirism_integrations.mca.client;

import de.teamlapen.vampirism_integrations.mca.MCARegistration;
import forge.net.mca.client.render.VillagerEntityMCARenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class ClientProxy {

    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        MCARegistration.FEMALE_AGGRESSIVE_VILLAGER.ifPresent(entry -> event.registerEntityRenderer(entry, VillagerEntityMCARenderer::new));
        MCARegistration.MALE_AGGRESSIVE_VILLAGER.ifPresent(entry -> event.registerEntityRenderer(entry, VillagerEntityMCARenderer::new));
        MCARegistration.FEMALE_CONVERTED_VILLAGER.ifPresent(entry -> event.registerEntityRenderer(entry, RenderVillagerMCAConverted::new));
        MCARegistration.MALE_CONVERTED_VILLAGER.ifPresent(entry -> event.registerEntityRenderer(entry, RenderVillagerMCAConverted::new));
    }
}
