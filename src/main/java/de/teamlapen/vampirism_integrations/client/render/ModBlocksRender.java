package de.teamlapen.vampirism_integrations.client.render;

import de.teamlapen.lib.lib.util.InventoryRenderHelper;
import de.teamlapen.vampirism_integrations.core.ModBlocks;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModBlocksRender {
    public static void register() {
        InventoryRenderHelper renderHelper = new InventoryRenderHelper(REFERENCE.MODID);
        renderHelper.registerRenderAllMeta(Item.getItemFromBlock(ModBlocks.blood_grinder), EnumFacing.HORIZONTALS);
        renderHelper.registerRender(ModBlocks.blood_sieve);
    }
}
