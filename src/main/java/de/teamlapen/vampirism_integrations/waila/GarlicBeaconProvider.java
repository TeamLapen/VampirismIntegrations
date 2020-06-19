package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.vampirism.tileentity.GarlicBeaconTileEntity;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;


public class GarlicBeaconProvider implements IComponentProvider {


    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        accessor.getStack().getItem().addInformation(accessor.getStack(), accessor.getWorld(), tooltip, ITooltipFlag.TooltipFlags.NORMAL);
        TileEntity t = accessor.getTileEntity();
        if (t instanceof GarlicBeaconTileEntity) {
            int fueled = ((GarlicBeaconTileEntity) t).getFuelTime();
            if (fueled > 0) {
                tooltip.add(new StringTextComponent("Fueled for " + (fueled / 20 / 20) + "min"));
            }
        }
    }

}