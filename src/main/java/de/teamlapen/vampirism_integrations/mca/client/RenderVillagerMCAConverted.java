package de.teamlapen.vampirism_integrations.mca.client;

import de.teamlapen.vampirism_integrations.mca.EntityConvertedVillagerMCA;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import mca.client.model.VillagerEntityModelMCA;
import mca.client.render.VillagerEntityMCARenderer;
import mca.client.render.VillagerLikeEntityMCARenderer;
import mca.entity.VillagerEntityMCA;
import mca.util.compat.model.Dilation;
import mca.util.compat.model.ModelData;
import mca.util.compat.model.TexturedModelData;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

class RenderVillagerMCAConverted extends VillagerEntityMCARenderer {
    private final static ResourceLocation overlay = new ResourceLocation(REFERENCE.MODID, "textures/mca/overlay.png");

    RenderVillagerMCAConverted(EntityRendererManager ctx) {
        super(ctx);
        this.addLayer(new LayerVampireFace<>(this, createModel(VillagerEntityModelMCA.bodyData(new Dilation(0.01F))).hideWears()));
    }

    private static VillagerEntityModelMCA<VillagerEntityMCA> createModel(ModelData data) {
        return new VillagerEntityModelMCA<>(TexturedModelData.of(data, 64, 64).createModel());
    }
}
