package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.VampirismAPI;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.List;

/**
 * Provides information about faction players
 */
class PlayerDataProvider implements IEntityComponentProvider {

    @Override
    public void appendBody(List<Component> tooltip, IEntityAccessor accessor, IPluginConfig config) {
        if (config.get(WailaPlugin.getShowPlayerInfoConf())) {
            if (accessor.getEntity() instanceof Player) {
                VampirismAPI.getFactionPlayerHandler((Player) accessor.getEntity()).ifPresent(fp -> {
                    if (fp.getCurrentLevel() > 0) {
                        tooltip.add(new TextComponent(String.format("%s %s: %s", fp.getCurrentFaction().getName().getString(), UtilLib.translate("text.vampirism.level"), fp.getCurrentLevel())).withStyle(style -> style.withColor(fp.getCurrentFaction().getChatColor())));
                    }
                });

            }
        }
    }


}