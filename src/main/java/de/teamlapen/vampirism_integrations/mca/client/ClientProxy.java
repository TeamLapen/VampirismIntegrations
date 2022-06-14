package de.teamlapen.vampirism_integrations.mca.client;

import de.teamlapen.vampirism_integrations.mca.EntityAngryVillagerMCA;
import de.teamlapen.vampirism_integrations.mca.EntityConvertedVillagerMCA;
import de.teamlapen.vampirism_integrations.mca.MCACompat;
import mca.client.render.VillagerEntityMCARenderer;

public class ClientProxy {

    public static void registerRenderer() {
        net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(EntityConvertedVillagerMCA.converted_villager_female, RenderVillagerMCAConverted::new);
        net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(EntityConvertedVillagerMCA.converted_villager_male, RenderVillagerMCAConverted::new);
        net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(EntityAngryVillagerMCA.angry_villager_female, VillagerEntityMCARenderer::new);
        net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(EntityAngryVillagerMCA.angry_villager_male, VillagerEntityMCARenderer::new);
    }


}
