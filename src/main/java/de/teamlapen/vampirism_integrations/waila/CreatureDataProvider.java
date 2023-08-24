package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import mcp.mobius.waila.api.*;
import mcp.mobius.waila.api.component.TextureComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

/**
 * Provide data for creatures
 */
class CreatureDataProvider implements IEntityComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        if (config.getBoolean(WailaPlugin.SHOW_CREATURE_INFO)) {
            if (accessor.getEntity() instanceof PathfinderMob && VReference.VAMPIRE_FACTION.getPlayerCapability(accessor.getPlayer()).map(IFactionPlayer::getLevel).orElse(0) > 0) {
                VampirismAPI.getExtendedCreatureVampirism(accessor.getEntity()).ifPresent(c -> {
                    int blood = c.getBlood();
                    ITooltipLine line = tooltip.addLine();

                    if (c.hasPoisonousBlood()) {
                        tooltip.addLine(Component.translatable("text.vampirism.blood.poisonous").withStyle(ChatFormatting.DARK_GREEN));
                    } else if (blood > 0) {
                        if (blood > config.getInt(WailaPlugin.MAX_BLOOD_ICONS_PER_LINE)) {
                            line.with(new TextureComponent(BloodComponent.ICONS, 8,8,9,0,9,9,256,256))
                                    .with(Component.literal(String.format("%d/%d", blood, c.getMaxBlood())).withStyle(ChatFormatting.RED));
                        } else {
                            int maxPerLine = config.getInt(WailaPlugin.MAX_LONG_BLOOD_MAX);
                            line.with(new BloodComponent(blood, c.getMaxBlood(), maxPerLine));
                        }
                    }
                });

            }
        }
    }



}