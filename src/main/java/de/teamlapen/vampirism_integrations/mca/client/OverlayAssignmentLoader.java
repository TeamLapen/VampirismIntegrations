package de.teamlapen.vampirism_integrations.mca.client;


import net.minecraft.command.CommandException;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Load mca skin -> overlay id from file
 */
public class OverlayAssignmentLoader {
    private final static Marker TAG = MarkerManager.getMarker("OverlayAssignment");
    private static final Logger LOGGER = LogManager.getLogger();


    static Map<String, Pair<Integer, Integer>> assignments;
    private static File dumpAssignmentFile;

    public static void init(File configDir) {
        File assignmentFile = new File(configDir, "vampirism/mca_skin_overlay_assignment.txt");
        dumpAssignmentFile = new File(configDir, "vampirism/mca_skin_overlay_assignment_dump.txt");

        try {
            assignments = loadAssignments(new InputStreamReader(OverlayAssignmentLoader.class.getResourceAsStream("/mca/skin_overlay_assignment.txt")), "skin_overlay_assignment.txt");
        } catch (IOException e) {
            LOGGER.warn(TAG, "Could not read mca skin assignments");
            LOGGER.error(e);
        }

        if (assignmentFile.exists()) {
            try {
                Map<String, Pair<Integer, Integer>> override = loadAssignments(new FileReader(assignmentFile), assignmentFile.getName());
                assignments.putAll(override);
                LOGGER.info(TAG, "Successfully loaded additional assignment file");
            } catch (IOException e) {
                LOGGER.error(TAG, "Could not read mca skin assignments from config file {}", assignmentFile.getName());
            }
        }

    }

    public static boolean save() {
        if (dumpAssignmentFile != null) {
            dumpAssignmentFile.getParentFile().mkdirs();
            try {
                return writeBloodValues(new FileWriter(dumpAssignmentFile), assignments, "Generated - Not loaded - Copy to mca_skin_overlay_assignment.txt - MCA Texture -> Eye Fang Overlay assignment. Format: texture=eye,fang. Use -1 for fang or eye to disable");
            } catch (IOException e) {
                LOGGER.error(TAG, "Failed to store mca skin assignments", e);
            }
        }
        return false;
    }


    private static Map<String, Pair<Integer, Integer>> loadAssignments(Reader r, String file) throws IOException {
        Map<String, Pair<Integer, Integer>> map = new HashMap<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(r);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) continue;
                if (StringUtils.isBlank(line)) continue;
                String[] p = line.split("=");
                if (p.length != 2) {
                    LOGGER.warn(TAG, "Line {} in {} is not formatted properly", line, file);
                    continue;
                }
                String[] p2 = p[1].split(",");
                int eye;
                int fang;
                try {
                    eye = Integer.parseInt(p2[0]);
                    fang = Integer.parseInt(p2[1]);
                } catch (NumberFormatException e) {
                    LOGGER.warn(TAG, "Line {}  in {} is not formatted properly", line, file);
                    continue;
                }
                map.put(p[0], Pair.of(eye, fang));
            }
        } finally {
            if (br != null) {
                br.close();
            }
            r.close();
        }
        return map;

    }

    private static boolean writeBloodValues(Writer w, Map<String, Pair<Integer, Integer>> values, String comment) throws IOException {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(w);
            bw.write('#');
            bw.write(comment);
            bw.newLine();
            for (Map.Entry<String, Pair<Integer, Integer>> entry : values.entrySet()) {
                bw.write(entry.getKey());
                bw.write('=');
                Pair<Integer, Integer> v = entry.getValue();
                bw.write(String.valueOf(v.getLeft()));
                bw.write(',');
                bw.write(String.valueOf((v.getRight())));
                bw.newLine();
            }
            bw.flush();
            return true;
        } catch (IOException e) {
            LOGGER.error(TAG,  "Failed to write mca overlay assignments ({})", comment);
            LOGGER.error(e);
        } finally {
            if (bw != null) {
                bw.close();
            }
            w.close();
        }
        return false;
    }

//    public static void registerSaveCommand() {
//        ClientCommandHandler.instance.registerCommand(new CommandBase() {
//            @Override
//            public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
//                if (OverlayAssignmentLoader.save()) {
//                    sender.sendMessage(new TextComponentString("Successfully stored mca skin assignments to vampirism config directory"));
//                }
//            }
//
//            @Override
//            public String getName() {
//                return "vampirism-mca-save-assignment";
//            }
//
//            @Override
//            public String getUsage(ICommandSender sender) {
//                return getName();
//            }
//        });
//    }
}
