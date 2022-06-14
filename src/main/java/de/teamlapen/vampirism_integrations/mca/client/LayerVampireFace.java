//package de.teamlapen.vampirism_integrations.mca.client;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//import com.mojang.blaze3d.platform.GlStateManager;
//import com.mojang.blaze3d.systems.RenderSystem;
//import de.teamlapen.vampirism_integrations.util.REFERENCE;
//import mca.client.model.VillagerEntityModelMCA;
//import mca.entity.VillagerEntityMCA;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.IRenderTypeBuffer;
//import net.minecraft.client.renderer.entity.layers.LayerRenderer;
//import net.minecraft.util.ResourceLocation;
//import org.apache.commons.lang3.tuple.Pair;
//
//public class LayerVampireFace extends LayerRenderer<VillagerEntityMCA, VillagerEntityModelMCA<VillagerEntityMCA>> {
//
//    private final RenderVillagerMCAConverted renderer;
//
//    private final ResourceLocation[] eyeOverlays;
//    private final ResourceLocation[] fangOverlays;
//
//
//    LayerVampireFace(RenderVillagerMCAConverted renderer, ResourceLocation overlay) {
//        super(renderer);
//        this.renderer = renderer;
//        eyeOverlays = new ResourceLocation[de.teamlapen.vampirism.REFERENCE.EYE_TYPE_COUNT];
//        for (int i = 0; i < eyeOverlays.length; i++) {
//            eyeOverlays[i] = new ResourceLocation(REFERENCE.VAMPIRISM_ID + ":textures/entity/vanilla/eyes" + (i) + ".png");
//        }
//        fangOverlays = new ResourceLocation[de.teamlapen.vampirism.REFERENCE.FANG_TYPE_COUNT];
//        for (int i = 0; i < fangOverlays.length; i++) {
//            fangOverlays[i] = new ResourceLocation(REFERENCE.VAMPIRISM_ID + ":textures/entity/vanilla/fangs" + i + ".png");
//        }
//    }
//
//    @Override
//    public void render(MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int i, VillagerEntityMCA villagerEntityMCA, float v, float v1, float v2, float v3, float v4, float v5) {
//        if (!villagerEntityMCA.isInvisible()) {
//            VillagerEntityModelMCA biped = renderer.getModel();
//            matrixStack.pushPose();
//            if (villagerEntityMCA.isShiftKeyDown()) {
//                matrixStack.translate(0.0F, 0.2F, 0.0F);
//            }
//            String tex = villagerEntityMCA.get(EntityVillagerMCA.TEXTURE);
//            Pair<Integer, Integer> conf = OverlayAssignmentLoader.assignments.computeIfAbsent(tex, k -> Pair.of(0, 0));
//
//            int eye = conf.getLeft();
//            int fang = conf.getRight();
//
//            if (eye >= 0 && eye < eyeOverlays.length) {
//                Minecraft.getInstance().getTextureManager().bind(eyeOverlays[eye]);
//                biped.bipedHead.render(scale);
//            }
//            if (fang >= 0 && fang < fangOverlays.length) {
//                Minecraft.getInstance().getTextureManager().bind(fangOverlays[eye]);
//                biped.bipedHead.render(scale);
//            }
//
//
//            matrixStack.popPose();
//        }
//    }
//}
