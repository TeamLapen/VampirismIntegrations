package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.VampirismAPI;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;

/**
 * Provides information about faction players
 */
class PlayerDataProvider implements IEntityComponentProvider {

    @Override
    public void appendBody(List<ITextComponent> tooltip, IEntityAccessor accessor, IPluginConfig config) {
        if (config.get(WailaPlugin.getShowPlayerInfoConf())) {
            if (accessor.getEntity() instanceof PlayerEntity) {
                VampirismAPI.getFactionPlayerHandler((PlayerEntity) accessor.getEntity()).ifPresent(fp -> {
                    if (fp.getCurrentLevel() > 0) {
                        tooltip.add(new StringTextComponent(String.format("%s %s: %s", fp.getCurrentFaction().getName().getString(), UtilLib.translate("text.vampirism.level"), fp.getCurrentLevel())).mergeStyle(fp.getCurrentFaction().getChatColor()));
                    }
                });

            }
        }
    }


}