package de.teamlapen.vampirism_integrations.mca.client;

import de.teamlapen.vampirism_integrations.mca.EntityConvertedVillagerMCA;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import mca.client.model.VillagerEntityModelMCA;
import mca.client.render.VillagerEntityMCARenderer;
import mca.client.render.VillagerLikeEntityMCARenderer;
import mca.entity.VillagerEntityMCA;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

class RenderVillagerMCAConverted extends VillagerEntityMCARenderer {
    private final static ResourceLocation overlay = new ResourceLocation(REFERENCE.MODID, "textures/mca/overlay.png");

    RenderVillagerMCAConverted(EntityRendererManager ctx) {
        super(ctx);
        //this.addLayer(new LayerVampireFace(this, overlay));

    }
}
