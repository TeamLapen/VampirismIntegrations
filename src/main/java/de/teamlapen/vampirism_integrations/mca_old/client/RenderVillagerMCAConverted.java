package de.teamlapen.vampirism_integrations.mca.client;

import de.teamlapen.vampirism_integrations.mca.EntityConvertedVillagerMCA;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import mca.client.render.RenderVillagerMCA;
import mca.entity.EntityVillagerMCA;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
class RenderVillagerMCAConverted extends RenderVillagerMCA<EntityConvertedVillagerMCA> {
    private final static ResourceLocation overlay = new ResourceLocation(REFERENCE.MODID, "textures/mca/overlay.png");

    RenderVillagerMCAConverted(RenderManager manager) {
        super(manager);
        this.addLayer(new LayerVampireFace(this, overlay));

    }

    @Override
    public void doRender(EntityVillagerMCA entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
