package de.teamlapen.vampirism_integrations.mca.client;

import de.teamlapen.vampirism_integrations.mca.EntityConvertedVillagerMCA;

public class ClientProxy {

    public static void registerRenderer() {
        net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(EntityConvertedVillagerMCA.class, RenderVillagerMCAConverted::new);
    }

}
