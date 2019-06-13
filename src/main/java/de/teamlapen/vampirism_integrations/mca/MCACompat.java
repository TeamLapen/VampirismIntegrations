package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.vampirism_integrations.IModCompat;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism_integrations.VampirismIntegrationsMod;
import de.teamlapen.vampirism_integrations.mca.client.ClientProxy;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import mca.entity.EntityVillagerMCA;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
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


    /**
     * MCA requires the assets to be present in the mods dir to read the skin files.
     * In the dev environment MCA is retrieved via gradle/maven and injected into the classpath without it being present in the mod dir.
     * That is annoying.
     * <p>
     * This method extracts the assets from the loaded mod into a extra file in the mod dir.
     *
     * @throws IOException
     */
    private static void extractAssetsForDev() throws IOException {
        VampirismIntegrationsMod.log.i(ID, "Preparing to extract MCA assets to mod dir");
        File modsDir = new File(Loader.instance().getConfigDir().getParentFile().getCanonicalPath(), "mods");
        if (!modsDir.exists()) {
            VampirismIntegrationsMod.log.e(ID, "Cannot find mods dir");
            return;
        }
        File versionInfo = new File(modsDir, "extractedModsInfo.cfg");
        File mcaAssetFile = new File(modsDir, "mcaAssets.zip");

        Configuration info = new Configuration(versionInfo);
        Property oldVersionProp = info.get("general", "mca_version", "0.0");

        boolean oldFileExist = mcaAssetFile.exists();

        ModContainer mcaContainer = null;
        for (ModContainer m : Loader.instance().getModList()) {
            if (ID.equals(m.getModId())) {
                mcaContainer = m;
                break;
            }
        }

        if (mcaContainer == null) {
            VampirismIntegrationsMod.log.e(ID, "Cannot find MCA container");
            return;
        }


        if (oldFileExist) {
            if (oldVersionProp.getString().equals(mcaContainer.getVersion())) {
                VampirismIntegrationsMod.log.i(ID, "Assets already present");
                return;
            } else {
                VampirismIntegrationsMod.log.i(ID, "Assets present but wrong version -> Deleting");
                mcaAssetFile.delete();
            }

        }

        oldVersionProp.set(mcaContainer.getVersion());


        VampirismIntegrationsMod.log.d(ID, "Preparing to copy assets");


        FileOutputStream dest = new FileOutputStream(mcaAssetFile);


        // out.setMethod(ZipOutputStream.DEFLATED);
        try (ZipOutputStream out = new ZipOutputStream(
                new BufferedOutputStream(dest))) {
            ZipFile mcaMod = new ZipFile(mcaContainer.getSource());
            Enumeration<? extends ZipEntry> entries = mcaMod.entries();

            while (entries.hasMoreElements()) {
                ZipEntry j = entries.nextElement();
                if (j.getName().startsWith("assets")) {
                    out.putNextEntry(j);

                    byte[] data = new byte[1024];


                    int count;
                    try (InputStream in = mcaMod.getInputStream(j)) {
                        while ((count = in.read(data, 0, 1024)) != -1) {
                            out.write(data, 0, count);
                        }
                    } catch (IOException e) {
                        VampirismIntegrationsMod.log.e(ID, e, "Failed read from input file");
                        throw e;
                    }
                }

            }
        } catch (IOException e) {
            VampirismIntegrationsMod.log.e(ID, e, "Failed to write to output zip file");
        }

        info.save();
        VampirismIntegrationsMod.log.i(ID, "Finished extracting assets");


    }

    @Override
    public String getModID() {
        return ID;
    }

    @Nullable
    @Override
    public String getAcceptedVersionRange() {
        return "[1.12.2-5.2.0,1.12.2-5.4.0)";
    }

    @Override
    public void loadConfigs(Configuration config, ConfigCategory category) {
        villager_blood_value = config.getInt("villager_blood_value", category.getName(), 15, 1, 1000, "");
    }

    @Override
    public void onInitStep(Step step, FMLStateEvent event) {
        if (step == Step.PRE_INIT) {
            if (VampirismIntegrationsMod.inDev) {
                try {
                    extractAssetsForDev();
                } catch (IOException e) {
                    VampirismIntegrationsMod.log.e(ID, e, "Could not extract assets");
                }
            }
            VampirismAPI.biteableRegistry().addBloodValue(new ResourceLocation(ID, VILLAGER_ID), villager_blood_value);
            VampirismAPI.biteableRegistry().addConvertible(EntityVillagerMCA.class, null, new EntityConvertedVillagerMCA.ConvertingHandler());
            if (FMLCommonHandler.instance().getSide().isClient()) {
                ClientProxy.registerRenderer();
            }
            MinecraftForge.EVENT_BUS.register(new EventHandlerMCA());
        } else if (step == Step.INIT) {
            int baseEntityId = 0;
            EntityRegistry.registerModEntity(new ResourceLocation(REFERENCE.MODID, CONVERTED_VILLAGER_ID), EntityConvertedVillagerMCA.class, EntityConvertedVillagerMCA.class.getSimpleName(), baseEntityId++, VampirismIntegrationsMod.instance, 50, 2, true);
            EntityRegistry.registerModEntity(new ResourceLocation(REFERENCE.MODID, ANGRY_VILLAGER_ID), EntityAngryVillagerMCA.class, EntityAngryVillagerMCA.class.getSimpleName(), baseEntityId++, VampirismIntegrationsMod.instance, 50, 2, true);
        }
    }
}
