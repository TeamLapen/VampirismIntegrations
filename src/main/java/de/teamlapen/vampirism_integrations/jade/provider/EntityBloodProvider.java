package de.teamlapen.vampirism_integrations.jade.provider;

import de.teamlapen.vampirism.entity.ExtendedCreature;
import de.teamlapen.vampirism_integrations.jade.elements.BloodElement;
import de.teamlapen.vampirism_integrations.jade.JadePlugin;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum EntityBloodProvider implements IEntityComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        ExtendedCreature.getSafe(entityAccessor.getEntity()).ifPresent(x -> {
            iTooltip.add(new BloodElement(x.getMaxBlood(), x.getBlood()));
        });
    }

    @Override
    public ResourceLocation getUid() {
        return JadePlugin.ENTITY_BLOOD;
    }
}
