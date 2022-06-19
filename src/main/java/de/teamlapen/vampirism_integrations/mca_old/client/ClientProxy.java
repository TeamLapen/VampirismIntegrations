package de.teamlapen.vampirism_integrations.mca.client;

import de.teamlapen.vampirism_integrations.mca.EntityAngryVillagerMCA;
import de.teamlapen.vampirism_integrations.mca.EntityConvertedVillagerMCA;
import mca.client.render.RenderVillagerMCA;

public class ClientProxy {

    public static void registerRenderer() {
        net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(EntityConvertedVillagerMCA.class, RenderVillagerMCAConverted::new);
        net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(EntityAngryVillagerMCA.class, RenderVillagerMCA::new);

    }


}
