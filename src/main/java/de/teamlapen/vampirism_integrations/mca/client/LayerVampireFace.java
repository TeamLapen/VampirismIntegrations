package de.teamlapen.vampirism_integrations.mca.client;

import de.teamlapen.vampirism_integrations.util.REFERENCE;
import mca.client.model.VillagerEntityModelMCA;
import mca.client.render.layer.VillagerLayer;
import mca.entity.VillagerLike;
import mca.entity.ai.Genetics;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;

public class LayerVampireFace<T extends MobEntity & VillagerLike<T>> extends VillagerLayer<T, VillagerEntityModelMCA<T>> {

    private final ResourceLocation[] eyeOverlays;
    private final ResourceLocation[] fangOverlays;


    LayerVampireFace(IEntityRenderer<T, VillagerEntityModelMCA<T>> renderer, VillagerEntityModelMCA<T> model) {
        super(renderer, model);
        this.eyeOverlays = new ResourceLocation[de.teamlapen.vampirism.REFERENCE.EYE_TYPE_COUNT];
        for (int i = 0; i < this.eyeOverlays.length; i++) {
            this.eyeOverlays[i] = new ResourceLocation(REFERENCE.VAMPIRISM_ID + ":textures/entity/vanilla/eyes" + (i) + ".png");
        }
        this.fangOverlays = new ResourceLocation[de.teamlapen.vampirism.REFERENCE.FANG_TYPE_COUNT];
        for (int i = 0; i < this.fangOverlays.length; i++) {
            this.fangOverlays[i] = new ResourceLocation(REFERENCE.VAMPIRISM_ID + ":textures/entity/vanilla/fangs" + i + ".png");
        }
    }

    @Override
    protected ResourceLocation getSkin(T villager) {
        int totalFaces = this.eyeOverlays.length;
        int index = (int)Math.min((float)(totalFaces - 1), Math.max(0.0F, villager.getGenetics().getGene(Genetics.FACE) * (float)totalFaces));
        return eyeOverlays[index];
    }

    @Override
    protected ResourceLocation getOverlay(T villager) {
        int totalFangs = this.fangOverlays.length;
        int index = (int)Math.min((float)(totalFangs - 1), Math.max(0.0F, villager.getGenetics().getGene(Genetics.FACE) * (float)totalFangs));
        return fangOverlays[index];
    }

}
