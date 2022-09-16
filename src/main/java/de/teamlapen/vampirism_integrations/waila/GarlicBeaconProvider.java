package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.vampirism.blockentity.GarlicDiffuserBlockEntity;
import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;


public class GarlicBeaconProvider implements IBlockComponentProvider {


    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        ItemStack stack = accessor.getStack();
        List<Component> l = new ArrayList<>();
        stack.getItem().appendHoverText(stack, accessor.getWorld(), l, TooltipFlag.Default.NORMAL);
        l.forEach(tooltip::addLine);
        BlockEntity t = accessor.getBlockEntity();
        if (t instanceof GarlicDiffuserBlockEntity) {
            int fueled = ((GarlicDiffuserBlockEntity) t).getFuelTime();
            if (fueled > 0) {
                tooltip.addLine(Component.literal("Fueled for " + (fueled / 20 / 20) + "min"));
            }
        }
    }

}