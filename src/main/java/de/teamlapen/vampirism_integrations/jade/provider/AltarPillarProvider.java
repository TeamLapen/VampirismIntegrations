package de.teamlapen.vampirism_integrations.jade.provider;

import de.teamlapen.vampirism.blocks.AltarPillarBlock;
import de.teamlapen.vampirism.blocks.WeaponTableBlock;
import de.teamlapen.vampirism_integrations.jade.JadePlugin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public enum AltarPillarProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BlockState blockState = blockAccessor.getBlockState();
        AltarPillarBlock.EnumPillarType value = blockState.getValue(AltarPillarBlock.TYPE_PROPERTY);
        IElementHelper helper = IElementHelper.get();
        int meta = value.meta;
        if (meta > 0) {
            iTooltip.add(helper.smallItem(value.fillerBlock.asItem().getDefaultInstance()));
            iTooltip.append(helper.spacer(5, 0));
            iTooltip.append(helper.text(Component.translatable("text.vampirism_integrations.value", meta)));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePlugin.ALTAR_PILLAR;
    }
}
