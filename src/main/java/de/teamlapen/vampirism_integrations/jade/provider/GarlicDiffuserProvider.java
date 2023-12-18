package de.teamlapen.vampirism_integrations.jade.provider;

import de.teamlapen.vampirism.blockentity.GarlicDiffuserBlockEntity;
import de.teamlapen.vampirism.core.ModItems;
import de.teamlapen.vampirism_integrations.jade.JadePlugin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.IProgressStyle;
import snownee.jade.impl.ui.ProgressElement;

public enum GarlicDiffuserProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getServerData().contains("garlicDiffuser", CompoundTag.TAG_COMPOUND)) {
            CompoundTag tag = blockAccessor.getServerData().getCompound("garlicDiffuser");
            int fuelTime = tag.getInt("fuelTime");
            float bootProgress = tag.getFloat("bootProgress");
            IElementHelper helper = IElementHelper.get();
            if (bootProgress == 1f) {
                iTooltip.add(helper.smallItem(new ItemStack(ModItems.PURIFIED_GARLIC.get())));
                iTooltip.append(IThemeHelper.get().seconds(fuelTime));
            } else {
                iTooltip.add(new ProgressElement(bootProgress, Component.translatable("text.vampirism.fog_diffuser.booting"), helper.progressStyle().color(0xD0D0FF).textColor(0xFFFFFF), new BoxStyle(), false));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof GarlicDiffuserBlockEntity diffuser) {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putInt("fuelTime", diffuser.getFuelTime());
            compoundTag.putFloat("bootProgress", diffuser.getBootProgress());
            tag.put("garlicDiffuser", compoundTag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePlugin.GARLIC_BEACON;
    }
}
