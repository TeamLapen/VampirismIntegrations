package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.VampirismAPI;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

/**
 * Provides information about faction players
 */
class PlayerDataProvider implements IEntityComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        if (config.getBoolean(WailaPlugin.getShowPlayerInfoConf())) {
            if (accessor.getEntity() instanceof Player) {
                VampirismAPI.getFactionPlayerHandler(accessor.getEntity()).ifPresent(fph -> {
                    if (fph.getCurrentLevel() > 0) {
                        fph.getCurrentFactionPlayer().ifPresent(fp -> {
                            tooltip.addLine(Component.literal(String.format("%s %s: %s", fp.getDisguisedAs().getName().getString(), UtilLib.translate("text.vampirism.level"), fph.getCurrentLevel())).withStyle(style -> style.withColor(fp.getDisguisedAs().getChatColor())));
                        });
                    }
                });

            }
        }
    }


}