package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.vampirism.blocks.BlockGarlicBeacon;
import de.teamlapen.vampirism.blocks.BlockWeaponTable;
import de.teamlapen.vampirism.tileentity.TileAltarInspiration;
import de.teamlapen.vampirism.tileentity.TileBloodContainer;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class WailaHandler {


    public static void onRegister(IWailaRegistrar registrar) {
        registrar.addConfig(REFERENCE.VAMPIRISM_ID, getShowCreatureInfoConf(), true);
        registrar.addConfig(REFERENCE.VAMPIRISM_ID, getShowPlayerInfoConf(), true);

        registrar.registerBodyProvider(new CreatureDataProvider(), EntityCreature.class);
        registrar.registerBodyProvider(new PlayerDataProvider(), EntityPlayer.class);
        IWailaDataProvider tankDataProvider = new TankDataProvider();
        registrar.registerBodyProvider(tankDataProvider, TileAltarInspiration.class);
        registrar.registerBodyProvider(tankDataProvider, TileBloodContainer.class);
        StackProviderIgnoreMeta stackProviderIgnoreMeta = new StackProviderIgnoreMeta();
        registrar.registerStackProvider(stackProviderIgnoreMeta, BlockWeaponTable.class);
        GarlicBeaconProvider garlicBeaconProvider = new GarlicBeaconProvider();
        registrar.registerBodyProvider(garlicBeaconProvider, BlockGarlicBeacon.class);
        registrar.registerStackProvider(garlicBeaconProvider, BlockGarlicBeacon.class);
    }

    static String getShowCreatureInfoConf() {
        return REFERENCE.MODID + ".showCreatureInfo";
    }

    static String getShowPlayerInfoConf() {
        return REFERENCE.MODID + ".showPlayerInfo";
    }

    private static class StackProviderIgnoreMeta implements IWailaDataProvider {

        @Nonnull
        @Override
        public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
            ItemStack stack = accessor.getStack();
            stack.setItemDamage(0);
            return stack;
        }

    }
}