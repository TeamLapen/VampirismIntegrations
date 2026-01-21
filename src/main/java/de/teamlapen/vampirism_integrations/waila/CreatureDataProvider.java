package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.IFactionMob;
import de.teamlapen.vampirism_integrations.Helper;
import mcp.mobius.waila.api.*;
import mcp.mobius.waila.api.component.TextureComponent;
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
            if (accessor.getEntity() instanceof PathfinderMob mob) {
                if(Helper.isVampire(accessor.getPlayer())){ //Show blood for vampire players
                    var c = VampirismAPI.extendedCreatureVampirism(accessor.getEntity());
                    int blood = c.getBlood();
                    if (c.hasPoisonousBlood()) {
                        tooltip.addLine(Component.translatable("text.vampirism.blood.poisonous").withStyle(ChatFormatting.DARK_GREEN));
                    } else if (blood > 0) {
                        ITooltipLine line = tooltip.addLine();
                        if (blood > config.getInt(WailaPlugin.MAX_BLOOD_ICONS_PER_LINE)) {
                            line.with(new TextureComponent(BloodComponent.HALF_TEXTURE_PATH, 8,8,0,0,9,9,9,9))
                                    .with(Component.literal(String.format("%d/%d", blood, c.getMaxBlood())).withStyle(ChatFormatting.RED));
                        } else {
                            int maxPerLine = config.getInt(WailaPlugin.MAX_LONG_BLOOD_MAX);
                            line.with(new BloodComponent(blood, c.getMaxBlood(), maxPerLine));
                        }
                    }
                }
                if(mob instanceof IFactionMob factionMob){ //Add faction
                    tooltip.addLine(factionMob.getFaction().getName().copy().withStyle(style->style.withColor(factionMob.getFaction().getChatColor())));
                }
            }
        }
    }



}