package de.teamlapen.vampirism_integrations.compat.waila;

import de.teamlapen.vampirism.api.VReference;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Provides information about the fluid level in blood containers
 */
class TankDataProvider implements IWailaDataProvider {

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getBlock() instanceof ITileEntityProvider && accessor.getTileEntity().hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, accessor.getSide())) {
            net.minecraftforge.fluids.capability.IFluidHandler fluidHandler = accessor.getTileEntity().getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, accessor.getSide());
            for (IFluidTankProperties info : fluidHandler.getTankProperties()) {
                FluidStack c = info.getContents();
                if (c != null) {
                    currenttip.add(String.format("%s%s: %d/%d", TextFormatting.RED, c.getLocalizedName(), c.amount / VReference.FOOD_TO_FLUID_BLOOD, info.getCapacity() / VReference.FOOD_TO_FLUID_BLOOD));
                }
            }
        }
        return currenttip;
    }


}