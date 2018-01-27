package de.teamlapen.vampirism_integrations.compat.mca.client;

import de.teamlapen.vampirism_integrations.compat.mca.EntityConvertedVillagerMCA;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerVampireFace implements LayerRenderer<EntityConvertedVillagerMCA> {

    private final RenderVillagerMCAConverted renderer;
    private final ResourceLocation overlay;

    public LayerVampireFace(RenderVillagerMCAConverted renderer, ResourceLocation overlay) {
        this.renderer = renderer;
        this.overlay = overlay;
    }

    @Override
    public void doRenderLayer(EntityConvertedVillagerMCA entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ModelBiped biped = (ModelBiped) renderer.getMainModel();
        GlStateManager.pushMatrix();
        if (entitylivingbaseIn.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
        }

        this.renderer.bindTexture(overlay);
        biped.bipedHead.render(scale);

        GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
