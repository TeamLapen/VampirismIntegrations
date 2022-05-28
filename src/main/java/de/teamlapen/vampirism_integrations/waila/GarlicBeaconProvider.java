package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.vampirism.blockentity.GarlicDiffuserBlockEntity;
import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;


public class GarlicBeaconProvider implements IComponentProvider {


    @Override
    public void appendBody(List<Component> tooltip, IBlockAccessor accessor, IPluginConfig config) {
        ItemStack stack = accessor.getStack();
        stack.getItem().appendHoverText(stack, accessor.getWorld(), tooltip, TooltipFlag.Default.NORMAL);
        BlockEntity t = accessor.getBlockEntity();
        if (t instanceof GarlicDiffuserBlockEntity) {
            int fueled = ((GarlicDiffuserBlockEntity) t).getFuelTime();
            if (fueled > 0) {
                tooltip.add(new TextComponent("Fueled for " + (fueled / 20 / 20) + "min"));
            }
        }
    }

}