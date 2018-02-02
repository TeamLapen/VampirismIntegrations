package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.factions.IFactionPlayerHandler;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Provides information about faction players
 */
class PlayerDataProvider implements IWailaEntityProvider {

    @Nonnull
    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        if (config.getConfig(WailaHandler.getShowPlayerInfoConf())) {
            if (entity instanceof EntityPlayer) {
                IFactionPlayerHandler factionHandler = VampirismAPI.getFactionPlayerHandler((EntityPlayer) entity);
                if (factionHandler.getCurrentLevel() > 0) {
                    currenttip.add(factionHandler.getCurrentFaction().getChatColor() + String.format("%s %s: %s", UtilLib.translate(factionHandler.getCurrentFaction().getUnlocalizedName()), UtilLib.translate("text.vampirism.level"), factionHandler.getCurrentLevel()));
                }
            }
        }
        return currenttip;
    }


}