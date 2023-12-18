package de.teamlapen.vampirism_integrations.jade.provider;

import de.teamlapen.vampirism.api.items.IBloodChargeable;
import de.teamlapen.vampirism.api.items.IItemWithTier;
import de.teamlapen.vampirism.blockentity.PedestalBlockEntity;
import de.teamlapen.vampirism.config.VampirismConfig;
import de.teamlapen.vampirism.items.VampirismVampireSwordItem;
import de.teamlapen.vampirism_integrations.jade.JadePlugin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;

public enum PedestalProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getServerData().contains("charge", CompoundTag.TAG_FLOAT)) {
            float charge = blockAccessor.getServerData().getFloat("charge");
            IElementHelper style = IElementHelper.get();
            iTooltip.add(style.progress(charge, null, style.progressStyle().color(0xEEFF1111).textColor(0xFF00FF00), new BoxStyle(), false));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof PedestalBlockEntity pedestal) {
            ItemStack stackForRender = pedestal.getStackForRender();
            if (stackForRender.getItem() instanceof IBloodChargeable) {
                CompoundTag swordTag = stackForRender.getTag();
                float charge = 0;
                if (swordTag != null) {
                    charge = swordTag.getFloat("charged");
                }
                compoundTag.putFloat("charge", charge);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePlugin.PEDESTAL_CHARGING;
    }
}
