package de.teamlapen.vampirism_integrations.jade.provider;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.factions.IFaction;
import de.teamlapen.vampirism.blockentity.TotemBlockEntity;
import de.teamlapen.vampirism_integrations.jade.JadePlugin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.Optional;

public enum TotemProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getServerData().contains("totem", CompoundTag.TAG_COMPOUND)) {
            CompoundTag tag = blockAccessor.getServerData().getCompound("totem");
            if (tag.contains("controllingFaction")) {
                IFaction<?> controlling = tag.contains("controllingFaction") ? VampirismAPI.factionRegistry().getFactionByID(new ResourceLocation(tag.getString("controllingFaction"))) : null;
                IFaction<?> capturing = tag.contains("capturingFaction") ? VampirismAPI.factionRegistry().getFactionByID(new ResourceLocation(tag.getString("capturingFaction"))) : null;
                if (capturing != null) {
                    if (controlling != null) {
                        iTooltip.add(Component.translatable("text.vampirism_integrations.defending").append(": ").append(controlling.getNamePlural().plainCopy().withStyle(s -> s.withColor(controlling.getChatColor()))));
                    }
                    iTooltip.add(Component.translatable("text.vampirism_integrations.attacking").append(": ").append(capturing.getNamePlural().plainCopy().withStyle(s -> s.withColor(capturing.getChatColor()))));
                } else if (controlling != null) {
                    iTooltip.add(Component.translatable("text.vampirism_integrations.controlling").append(": ").append(controlling.getNamePlural().plainCopy().withStyle(s -> s.withColor(controlling.getChatColor()))));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof TotemBlockEntity totem) {
            CompoundTag compoundTag = new CompoundTag();
            Optional.ofNullable(totem.getControllingFaction()).map(IFaction::getID).ifPresent(id -> compoundTag.putString("controllingFaction", id.toString()));
            Optional.ofNullable(totem.getCapturingFaction()).map(IFaction::getID).ifPresent(id -> compoundTag.putString("capturingFaction", id.toString()));
            tag.put("totem", compoundTag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePlugin.TOTEM;
    }
}
