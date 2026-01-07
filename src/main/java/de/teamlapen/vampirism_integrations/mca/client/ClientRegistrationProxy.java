package de.teamlapen.vampirism_integrations.mca.client;

import de.teamlapen.vampirism_integrations.mca.MCARegistration;
import net.conczin.mca.client.render.VillagerEntityMCARenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;


public class ClientRegistrationProxy {

    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer( MCARegistration.FEMALE_AGGRESSIVE_VILLAGER.get(), VillagerEntityMCARenderer::new);
        event.registerEntityRenderer(MCARegistration.MALE_AGGRESSIVE_VILLAGER.get(), VillagerEntityMCARenderer::new);
        event.registerEntityRenderer(MCARegistration.FEMALE_CONVERTED_VILLAGER.get(), RenderVillagerMCAConverted::new);
        event.registerEntityRenderer(MCARegistration.MALE_CONVERTED_VILLAGER.get(), RenderVillagerMCAConverted::new);
    }
}
