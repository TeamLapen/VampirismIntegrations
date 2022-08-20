package de.teamlapen.vampirism_integrations.mca.client;

import de.teamlapen.vampirism_integrations.util.REFERENCE;
import forge.net.mca.client.model.VillagerEntityModelMCA;
import forge.net.mca.client.render.layer.VillagerLayer;
import forge.net.mca.entity.VillagerLike;
import forge.net.mca.entity.ai.Genetics;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class LayerVampireFace<T extends Mob & VillagerLike<T>> extends VillagerLayer<T, VillagerEntityModelMCA<T>> {

    private final ResourceLocation[] eyeOverlays;
    private final ResourceLocation[] fangOverlays;

    public LayerVampireFace(RenderLayerParent<T, VillagerEntityModelMCA<T>> renderer, VillagerEntityModelMCA<T> model) {
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
