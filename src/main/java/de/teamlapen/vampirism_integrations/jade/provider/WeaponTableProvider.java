package de.teamlapen.vampirism_integrations.jade.provider;

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

public enum WeaponTableProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BlockState blockState = blockAccessor.getBlockState();
        int value = blockState.getValue(WeaponTableBlock.LAVA);
        IElementHelper helper = IElementHelper.get();
        if (value > 0) {
            iTooltip.add(helper.smallItem(Items.LAVA_BUCKET.getDefaultInstance()));
        } else {
            iTooltip.add(helper.smallItem(Items.BUCKET.getDefaultInstance()));
        }
        iTooltip.append(helper.spacer(5, 0));
        iTooltip.append(helper.text(Component.literal("%s/%s".formatted(value, WeaponTableBlock.MAX_LAVA))));
    }

    @Override
    public ResourceLocation getUid() {
        return JadePlugin.WEAPON_TABLE;
    }
}
