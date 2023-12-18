package de.teamlapen.vampirism_integrations.jade.provider;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.factions.IFaction;
import de.teamlapen.vampirism.api.entity.factions.IFactionPlayerHandler;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.entity.factions.FactionPlayerHandler;
import de.teamlapen.vampirism_integrations.jade.JadePlugin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.impl.config.PluginConfig;

public enum PlayerFactionProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        if (entityAccessor.getServerData().contains("playerFaction", Tag.TAG_COMPOUND)) {
            CompoundTag tag = entityAccessor.getServerData().getCompound("playerFaction");
            if (tag.contains("faction")) {
                String factionId = tag.getString("faction");
                int level = tag.getInt("level");
                IPlayableFaction<?> faction = ((IPlayableFaction<?>) VampirismAPI.factionRegistry().getFactionByID(new ResourceLocation(factionId)));
                MutableComponent component = Component.translatable(faction.getName().getString()).append(": ");
                if (tag.contains("lordTitle")) {
                    int lordLevel = tag.getInt("lordLevel");
                    if (PluginConfig.INSTANCE.get(JadePlugin.PLAYER_FACTION_LORD_LEVEL_NUMBER)) {
                        component.append(Component.translatable("text.vampirism.lord").append(" ").append(Component.translatable("text.vampirism.level"))).append(" ").append(String.valueOf(lordLevel));
                    } else {
                        ExtraCodecs.COMPONENT.decode(NbtOps.INSTANCE, tag.get("lordTitle")).result().map(Pair::getFirst).ifPresent(component::append);
                    }
                } else {
                    component.append(Component.translatable("text.vampirism.level")).append(" ").append(String.valueOf(level));
                }
                iTooltip.add(component.withStyle(style -> style.withColor(faction.getChatColor())));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePlugin.PLAYER_FACTION;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, EntityAccessor entityAccessor) {
        if (entityAccessor.getEntity() instanceof Player player) {
            FactionPlayerHandler.getOpt(player).ifPresent(handler -> {
                handler.getCurrentFactionPlayer().ifPresent(fp -> {
                    CompoundTag tag = new CompoundTag();
                    IFaction<?> currentFaction = fp.getDisguisedAs();
                    if (currentFaction != null)  {
                        tag.putString("faction", currentFaction.getID().toString());
                        tag.putInt("level", handler.getCurrentLevel());
                        if (handler.getLordLevel() > 0) {
                            tag.putInt("lordLevel", handler.getLordLevel());
                            DataResult<Tag> tagDataResult = ExtraCodecs.COMPONENT.encodeStart(NbtOps.INSTANCE, handler.getLordTitle());
                            tagDataResult.result().ifPresent(t -> {
                                tag.putInt("lordLevel", handler.getLordLevel());
                                tag.put("lordTitle", t);
                            });
                        }
                    }
                    compoundTag.put("playerFaction", tag);
                });
            });
        }
    }
}
