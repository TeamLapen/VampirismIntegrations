package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.VReference;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.List;

/**
 * Provides information about the fluid level in blood containers
 */
class TankDataProvider implements IComponentProvider {

    @Override
    public void appendBody(List<Component> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockState().hasBlockEntity()) {
            BlockEntity tileEntity = accessor.getBlockEntity();
            if (tileEntity != null) {
                tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, accessor.getSide()).ifPresent(fh -> {
                    for (int i = 0; i < fh.getTanks(); i++) {
                        FluidStack c = fh.getFluidInTank(i);
                        if (!c.isEmpty()) {
                            tooltip.add(new TextComponent(String.format("%s: %d/%d", UtilLib.translate(c.getTranslationKey()), c.getAmount() / VReference.FOOD_TO_FLUID_BLOOD, fh.getTankCapacity(i) / VReference.FOOD_TO_FLUID_BLOOD)).withStyle(ChatFormatting.RED));
                        }
                    }

                });
            }
        }
    }

}