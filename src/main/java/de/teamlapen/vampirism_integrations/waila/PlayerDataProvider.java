package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.VampirismAPI;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Provides information about faction players
 */
class PlayerDataProvider implements IEntityComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        if (config.getBoolean(WailaPlugin.getShowPlayerInfoConf())) {
            if (accessor.getEntity() instanceof Player) {
                VampirismAPI.getFactionPlayerHandler((Player) accessor.getEntity()).ifPresent(fp -> {
                    if (fp.getCurrentLevel() > 0) {
                        tooltip.addLine(Component.literal(String.format("%s %s: %s", fp.getCurrentFaction().getName().getString(), UtilLib.translate("text.vampirism.level"), fp.getCurrentLevel())).withStyle(style -> style.withColor(fp.getCurrentFaction().getChatColor())));
                    }
                });

            }
        }
    }


}