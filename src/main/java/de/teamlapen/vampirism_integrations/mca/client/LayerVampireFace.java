package de.teamlapen.vampirism_integrations.mca.client;

import de.teamlapen.vampirism_integrations.mca.EntityConvertedVillagerMCA;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import mca.entity.EntityVillagerMCA;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class LayerVampireFace implements LayerRenderer<EntityConvertedVillagerMCA> {

    private final RenderVillagerMCAConverted renderer;

    private final ResourceLocation[] eyeOverlays;
    private final ResourceLocation[] fangOverlays;


    LayerVampireFace(RenderVillagerMCAConverted renderer, ResourceLocation overlay) {
        this.renderer = renderer;
        eyeOverlays = new ResourceLocation[de.teamlapen.vampirism.util.REFERENCE.EYE_TYPE_COUNT];
        for (int i = 0; i < eyeOverlays.length; i++) {
            eyeOverlays[i] = new ResourceLocation(REFERENCE.VAMPIRISM_ID + ":textures/entity/vanilla/eyes" + (i) + ".png");
        }
        fangOverlays = new ResourceLocation[de.teamlapen.vampirism.util.REFERENCE.FANG_TYPE_COUNT];
        for (int i = 0; i < fangOverlays.length; i++) {
            fangOverlays[i] = new ResourceLocation(REFERENCE.VAMPIRISM_ID + ":textures/entity/vanilla/fangs" + i + ".png");
        }
    }

    @Override
    public void doRenderLayer(EntityConvertedVillagerMCA entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entitylivingbaseIn.isInvisible()) {
            ModelBiped biped = (ModelBiped) renderer.getMainModel();
            GlStateManager.pushMatrix();
            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            String tex = entitylivingbaseIn.get(EntityVillagerMCA.TEXTURE);
            Pair<Integer, Integer> conf = OverlayAssignmentLoader.assignments.computeIfAbsent(tex, k -> Pair.of(0, 0));

            int eye = conf.getLeft();
            int fang = conf.getRight();

            if (eye >= 0 && eye < eyeOverlays.length) {
                this.renderer.bindTexture(eyeOverlays[eye]);
                biped.bipedHead.render(scale);
            }
            if (fang >= 0 && fang < fangOverlays.length) {
                this.renderer.bindTexture(fangOverlays[fang]);
                biped.bipedHead.render(scale);
            }


            GlStateManager.popMatrix();
        }

    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
