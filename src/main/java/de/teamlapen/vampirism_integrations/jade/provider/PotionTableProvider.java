package de.teamlapen.vampirism_integrations.jade.provider;

import de.teamlapen.vampirism.blockentity.PotionTableBlockEntity;
import de.teamlapen.vampirism_integrations.jade.JadePlugin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.IElementHelper;

public enum PotionTableProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getServerData().contains("potionTable", 10)) {
            CompoundTag tag = blockAccessor.getServerData().getCompound("potionTable");
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
        if (blockAccessor.getBlockEntity() instanceof PotionTableBlockEntity potionTable && !potionTable.isEmpty()) {
            CompoundTag compoundTag1 = potionTable.saveWithoutMetadata();
            CompoundTag compound = new CompoundTag();
            compound.putInt("time", compoundTag1.getInt("BrewTime"));
            compound.putInt("fuel", compoundTag1.getInt("Fuel"));
            compoundTag.put("potionTable", compound);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePlugin.POTION_TABLE;
    }
}
