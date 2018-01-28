package de.teamlapen.vampirism_integrations.core;

import de.teamlapen.vampirism_integrations.blocks.BlockGrinder;
import de.teamlapen.vampirism_integrations.blocks.BlockSieve;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

import static de.teamlapen.lib.lib.util.UtilLib.getNull;

@GameRegistry.ObjectHolder(REFERENCE.MODID)
public class ModBlocks {

    public static final BlockGrinder blood_grinder = getNull();
    public static final BlockSieve blood_sieve = getNull();


    static void registerBlocks(IForgeRegistry<Block> registry) {
        registry.register(new BlockGrinder());
        registry.register(new BlockSieve());
    }

    static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.register(itemBlock(blood_grinder));
        registry.register(itemBlock(blood_sieve));
    }

    private static @Nonnull
    ItemBlock itemBlock(@Nonnull Block b) {
        ItemBlock item = new ItemBlock(b);
        //noinspection ConstantConditions
        item.setRegistryName(b.getRegistryName());
        return item;
    }
}
