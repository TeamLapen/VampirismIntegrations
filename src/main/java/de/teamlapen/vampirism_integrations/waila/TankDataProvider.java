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
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.Optional;

/**
 * Provides information about the fluid level in blood containers
 */
class TankDataProvider implements IBlockComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockState().hasBlockEntity()) {
            BlockEntity tileEntity = accessor.getBlockEntity();
            if (tileEntity != null) {
                Optional.ofNullable(Capabilities.FluidHandler.BLOCK.getCapability(accessor.getWorld(), accessor.getPosition(), accessor.getBlockState(), tileEntity, null)).ifPresent(fh -> {
                    for (int i = 0; i < fh.getTanks(); i++) {
                        FluidStack c = fh.getFluidInTank(i);
                        if (!c.isEmpty()) {
                            tooltip.addLine(Component.literal(String.format("%s: %d/%d", Component.translatable(c.getDescriptionId()).getString(), c.getAmount() / VReference.FOOD_TO_FLUID_BLOOD, fh.getTankCapacity(i) / VReference.FOOD_TO_FLUID_BLOOD)).withStyle(ChatFormatting.RED));
                        }
                    }
                });
            }
        }
    }

}