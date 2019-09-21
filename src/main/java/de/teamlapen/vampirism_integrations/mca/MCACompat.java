package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.vampirism_integrations.IModCompat;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism_integrations.VampirismIntegrationsMod;
import de.teamlapen.vampirism_integrations.mca.client.ClientProxy;
import de.teamlapen.vampirism_integrations.mca.client.OverlayAssignmentLoader;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import mca.entity.EntityVillagerMCA;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import javax.annotation.Nullable;
import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class MCACompat implements IModCompat {

    public static final String CONVERTED_VILLAGER_ID = "villagermcaconverted";
    public static final String ANGRY_VILLAGER_ID = "villagermcaangry";

    public static final String ID = "mca";
    protected static final String VILLAGER_ID = "villagermca";
    private int villager_blood_value;



    @Override
    public String getModID() {
        return ID;
    }

    @Nullable
    @Override
    public String getAcceptedVersionRange() {
        return "[1.12.2-6.0.0,)";
    }

    @Override
    public void loadConfigs(Configuration config, ConfigCategory category) {
        villager_blood_value = config.getInt("villager_blood_value", category.getName(), 15, 1, 1000, "");
    }

    @Override
    public void onInitStep(Step step, FMLStateEvent event) {
        if (step == Step.PRE_INIT) {

            VampirismAPI.biteableRegistry().addBloodValue(new ResourceLocation(ID, VILLAGER_ID), villager_blood_value);
            VampirismAPI.biteableRegistry().addConvertible(EntityVillagerMCA.class, null, new EntityConvertedVillagerMCA.ConvertingHandler());
            if (FMLCommonHandler.instance().getSide().isClient()) {
                ClientProxy.registerRenderer();
                OverlayAssignmentLoader.init(((FMLPreInitializationEvent) event).getModConfigurationDirectory());
                OverlayAssignmentLoader.registerSaveCommand();
            }
            MinecraftForge.EVENT_BUS.register(new EventHandlerMCA());
        } else if (step == Step.INIT) {
            int baseEntityId = 0;
            EntityRegistry.registerModEntity(new ResourceLocation(REFERENCE.MODID, CONVERTED_VILLAGER_ID), EntityConvertedVillagerMCA.class, EntityConvertedVillagerMCA.class.getSimpleName(), baseEntityId++, VampirismIntegrationsMod.instance, 50, 2, true);
            EntityRegistry.registerModEntity(new ResourceLocation(REFERENCE.MODID, ANGRY_VILLAGER_ID), EntityAngryVillagerMCA.class, EntityAngryVillagerMCA.class.getSimpleName(), baseEntityId++, VampirismIntegrationsMod.instance, 50, 2, true);

        }
    }
}
