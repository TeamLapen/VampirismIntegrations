package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.vampirism.tileentity.GarlicBeaconTileEntity;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.List;


public class GarlicBeaconProvider implements IComponentProvider {


    @Override
    public void appendBody(List<Component> tooltip, IDataAccessor accessor, IPluginConfig config) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> ClientUtil.gatherTooltips(accessor.getStack(), accessor.getWorld(), tooltip));
        BlockEntity t = accessor.getTileEntity();
        if (t instanceof GarlicBeaconTileEntity) {
            int fueled = ((GarlicBeaconTileEntity) t).getFuelTime();
            if (fueled > 0) {
                tooltip.add(new TextComponent("Fueled for " + (fueled / 20 / 20) + "min"));
            }
        }
    }

}