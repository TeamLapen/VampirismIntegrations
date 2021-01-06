package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.vampirism.tileentity.GarlicBeaconTileEntity;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.List;


public class GarlicBeaconProvider implements IComponentProvider {


    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> ClientUtil.gatherTooltips(accessor.getStack(), accessor.getWorld(), tooltip));
        TileEntity t = accessor.getTileEntity();
        if (t instanceof GarlicBeaconTileEntity) {
            int fueled = ((GarlicBeaconTileEntity) t).getFuelTime();
            if (fueled > 0) {
                tooltip.add(new StringTextComponent("Fueled for " + (fueled / 20 / 20) + "min"));
            }
        }
    }

}