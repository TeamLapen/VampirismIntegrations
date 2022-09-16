package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.VReference;
import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * Provides information about the fluid level in blood containers
 */
class TankDataProvider implements IBlockComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockState().hasBlockEntity()) {
            BlockEntity tileEntity = accessor.getBlockEntity();
            if (tileEntity != null) {
                tileEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, accessor.getSide()).ifPresent(fh -> {
                    for (int i = 0; i < fh.getTanks(); i++) {
                        FluidStack c = fh.getFluidInTank(i);
                        if (!c.isEmpty()) {
                            tooltip.addLine(Component.literal(String.format("%s: %d/%d", UtilLib.translate(c.getTranslationKey()), c.getAmount() / VReference.FOOD_TO_FLUID_BLOOD, fh.getTankCapacity(i) / VReference.FOOD_TO_FLUID_BLOOD)).withStyle(ChatFormatting.RED));
                        }
                    }

                });
            }
        }
    }

}