package de.teamlapen.vampirism_integrations.mca.client;

import de.teamlapen.vampirism.client.render.LayerVampireEntity;
import de.teamlapen.vampirism_integrations.mca.EntityConvertedVillagerMCA;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import mca.client.render.RenderVillagerMCA;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderVillagerMCAConverted extends RenderVillagerMCA<EntityConvertedVillagerMCA> {
    private final ResourceLocation overlay = new ResourceLocation(REFERENCE.VAMPIRISM_ID, "textures/entity/vanilla/zombie_overlay.png");

    public RenderVillagerMCAConverted(RenderManager manager) {
        super(manager);
        this.addLayer(new LayerVampireEntity(this, overlay, false));

    }
}
