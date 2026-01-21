package de.teamlapen.vampirism_integrations.jade.provider;

import de.teamlapen.vampirism.api.entity.IFactionMob;
import de.teamlapen.vampirism_integrations.jade.JadePlugin;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum EntityFactionProvider implements IEntityComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        if(entityAccessor.getEntity() instanceof IFactionMob factionMob){
            iTooltip.add(factionMob.getFaction().getName().copy().withStyle(style->style.withColor(factionMob.getFaction().getChatColor())));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePlugin.ENTITY_FACTION;
    }
}
