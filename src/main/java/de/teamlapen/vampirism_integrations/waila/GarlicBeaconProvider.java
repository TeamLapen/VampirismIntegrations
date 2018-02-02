package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.blocks.BlockGarlicBeacon;
import de.teamlapen.vampirism.tileentity.TileGarlicBeacon;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.util.List;


public class GarlicBeaconProvider implements IWailaDataProvider {




    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        itemStack.getItem().addInformation(itemStack, accessor.getWorld(), currenttip, ITooltipFlag.TooltipFlags.NORMAL);
        TileEntity t = accessor.getTileEntity();
        if (t != null && t instanceof TileGarlicBeacon) {
            int fueled = ((TileGarlicBeacon) t).getFuelTime();
            if (fueled > 0) {
                currenttip.add(UtilLib.translateFormatted("Fueled for %s min.", fueled / 20 / 20));
            }
        }
        return currenttip;
    }


    @Nonnull
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return new ItemStack(WailaModCompat.garlicBeacon, 1, accessor.getBlockState().getValue(BlockGarlicBeacon.TYPE).getId());

    }

}