package de.teamlapen.vampirism_integrations.mca.client;


import net.conczin.mca.client.model.VillagerEntityModelMCA;
import net.conczin.mca.client.render.VillagerEntityMCARenderer;
import net.conczin.mca.entity.VillagerEntityMCA;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderVillagerMCAConverted extends VillagerEntityMCARenderer {
    public RenderVillagerMCAConverted(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.addLayer(new LayerVampireFace<>(this,  createModel(VillagerEntityModelMCA.bodyData(new CubeDeformation(0.01F))).hideWears()));
    }

    private static VillagerEntityModelMCA<VillagerEntityMCA> createModel(MeshDefinition data) {
        return new VillagerEntityModelMCA(LayerDefinition.create(data, 64, 64).bakeRoot());
    }
}
