package de.teamlapen.vampirism_integrations.jade.provider;

import de.teamlapen.vampirism.blocks.HunterTableBlock;
import de.teamlapen.vampirism_integrations.jade.JadePlugin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public enum AltarOfInfusionProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        iTooltip.add(IElementHelper.get().text(Component.translatable("text.vampirism.for_to_levels", 5, 14)));
    }

    @Override
    public ResourceLocation getUid() {
        return JadePlugin.ALTAR_OF_INFUSION;
    }
}
