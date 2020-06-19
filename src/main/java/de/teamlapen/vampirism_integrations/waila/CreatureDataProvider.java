package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

/**
 * Provide data for creatures
 */
class CreatureDataProvider implements IEntityComponentProvider {

    @Override
    public void appendBody(List<ITextComponent> tooltip, IEntityAccessor accessor, IPluginConfig config) {
        if (config.get(WailaPlugin.getShowCreatureInfoConf())) {
            if (accessor.getEntity() instanceof CreatureEntity && VReference.VAMPIRE_FACTION.getPlayerCapability(accessor.getPlayer()).map(IFactionPlayer::getLevel).orElse(0) > 0) {
                VampirismAPI.getExtendedCreatureVampirism((CreatureEntity) accessor.getEntity()).ifPresent(c -> {
                    int blood = c.getBlood();
                    if (c.hasPoisonousBlood()) {
                        tooltip.add(new TranslationTextComponent("text.vampirism.blood.poisonous"));
                    } else if (blood > 0) {
                        tooltip.add(new StringTextComponent(String.format("%s%s: %d", TextFormatting.RED, UtilLib.translate("text.vampirism.entitysblood"), blood)));
                    }
                });

            }
        }
    }


}