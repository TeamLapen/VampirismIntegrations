package de.teamlapen.vampirism_integrations.waila;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.List;

/**
 * Place for "proxy" methods used together with DistExecutor to keep client imports from serveer
 */
public class ClientUtil {

    public static void gatherTooltips(ItemStack stack, World world, List<ITextComponent> tooltips) {
        stack.getItem().addInformation(stack, world, tooltips, ITooltipFlag.TooltipFlags.NORMAL);
    }
}
