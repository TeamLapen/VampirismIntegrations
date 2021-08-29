package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.VReference;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.List;

/**
 * Provides information about the fluid level in blood containers
 */
class TankDataProvider implements IComponentProvider {

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockState().hasTileEntity()) {
            TileEntity tileEntity = accessor.getTileEntity();
            if (tileEntity != null) {
                tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, accessor.getSide()).ifPresent(fh -> {
                    for (int i = 0; i < fh.getTanks(); i++) {
                        FluidStack c = fh.getFluidInTank(i);
                        if (!c.isEmpty()) {
                            tooltip.add(new StringTextComponent(String.format("%s: %d/%d", UtilLib.translate(c.getTranslationKey()), c.getAmount() / VReference.FOOD_TO_FLUID_BLOOD, fh.getTankCapacity(i) / VReference.FOOD_TO_FLUID_BLOOD)).withStyle(TextFormatting.RED));
                        }
                    }

                });
            }
        }
    }

}