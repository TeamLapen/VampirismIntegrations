package de.teamlapen.vampirism_integrations.mca.client;

import com.mojang.blaze3d.vertex.PoseStack;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import net.conczin.mca.MCA;
import net.conczin.mca.client.model.CommonVillagerModel;
import net.conczin.mca.client.model.VillagerEntityModelMCA;
import net.conczin.mca.client.render.layer.VillagerLayer;
import net.conczin.mca.entity.VillagerLike;
import net.conczin.mca.entity.ai.Genetics;
import net.conczin.mca.entity.ai.Traits;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class LayerVampireFace<T extends Mob & VillagerLike<T>> extends VillagerLayer<T, VillagerEntityModelMCA<T>> {
    private static final int FACE_COUNT = 22;

    public LayerVampireFace(RenderLayerParent<T, VillagerEntityModelMCA<T>> renderer, VillagerEntityModelMCA<T> model) {
        super(renderer, model);
    }

    @Override
    public void render(PoseStack transform, MultiBufferSource provider, int light, T villager, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        int time = villager.tickCount / 2 + (int)(CommonVillagerModel.getVillager(villager).getGenetics().getGene(Genetics.HEMOGLOBIN) * 65536.0F);
        boolean blink = time % 50 == 1 || time % 57 == 1 || villager.isSleeping() || villager.isDeadOrDying();
        if (!blink){ //Don't render vampire eyes when blinking
            this.model.setAllVisible(false);
            this.model.head.visible = true;
            super.render(transform, provider, light, villager, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
        }
    }

    @Override
    protected boolean isTranslucent() {
        return true;
    }

    @Override
    public ResourceLocation getSkin(T villager) {
        int index = (int)Math.min(21.0F, Math.max(0.0F, CommonVillagerModel.getVillager(villager).getGenetics().getGene(Genetics.FACE) * FACE_COUNT));

        String gender = CommonVillagerModel.getVillager(villager).getGenetics().getGender().getDataName();
        return this.cached("textures/mca/eye_overlay/" + gender + "/" + index + ".png", id-> ResourceLocation.fromNamespaceAndPath(REFERENCE.MODID, id));
    }

}
