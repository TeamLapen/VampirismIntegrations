package de.teamlapen.vampirism_integrations.jade.provider;

import de.teamlapen.vampirism.blockentity.AlchemyTableBlockEntity;
import de.teamlapen.vampirism_integrations.jade.JadePlugin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.IElementHelper;

public enum AlchemyTableProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getServerData().contains("alchemyTable", 10)) {
            CompoundTag tag = blockAccessor.getServerData().getCompound("alchemyTable");
            int fuel = tag.getInt("fuel");
            int time = tag.getInt("time");
            IElementHelper helper = IElementHelper.get();
            iTooltip.add(helper.smallItem(new ItemStack(Items.BLAZE_POWDER)));
            iTooltip.append(IThemeHelper.get().info(fuel));
            if (time > 0) {
                iTooltip.append(helper.spacer(5, 0));
                iTooltip.append(helper.smallItem(new ItemStack(Items.CLOCK)));
                iTooltip.append(IThemeHelper.get().seconds(time));
            }

        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof AlchemyTableBlockEntity alchemyTable && !alchemyTable.isEmpty()) {
            CompoundTag compoundTag1 = alchemyTable.saveWithoutMetadata();
            CompoundTag compound = new CompoundTag();
            compound.putInt("time", compoundTag1.getInt("BrewTime"));
            compound.putInt("fuel", compoundTag1.getInt("Fuel"));
            compoundTag.put("alchemyTable", compound);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePlugin.ALCHEMY_TABLE;
    }
}
