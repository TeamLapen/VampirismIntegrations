package de.teamlapen.vampirism_integrations.waila;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Place for "proxy" methods used together with DistExecutor to keep client imports from serveer
 */
public class ClientUtil {

    public static void gatherTooltips(ItemStack stack, Level world, List<Component> tooltips) {
        stack.getItem().appendHoverText(stack, world, tooltips, TooltipFlag.Default.NORMAL);
    }
}
