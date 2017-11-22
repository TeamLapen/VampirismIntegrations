package de.teamlapen.vampirism_integrations;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.lib.lib.util.VersionChecker;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.server.command.CommandTreeBase;

import java.util.List;
import java.util.Map;

public class Command extends CommandTreeBase {

    public Command() {
        this.addSubcommand(new CommandBase() {
            @Override
            public String getName() {
                return "loaded";
            }

            @Override
            public String getUsage(ICommandSender sender) {
                return getName();
            }

            @Override
            public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
                sender.sendMessage(new TextComponentString("Loaded and active mods:"));
                Map<String, ModContainer> activeMods = Loader.instance().getIndexedModList();
                sender.sendMessage(new TextComponentString("Vampirism Version: " + activeMods.get(REFERENCE.VAMPIRISM_ID).getVersion()));
//          TODO readd
//                for(IModCompat compat:VampirismIntegrationsMod.instance.compatLoader.getLoadedCompat){
//                    if(activeMods.containsKey(compat.getModID())){
//                        sender.sendMessage(new TextComponentString(String.format("Active: %s Version %s",compat.getModID(),activeMods.get(compat.getModID()).getVersion())));
//                    }
//
//                }
            }
        });
        addSubcommand(new CommandBase() {


            @Override
            public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
                if (!VampirismIntegrationsMod.instance.getVersionInfo().isNewVersionAvailable()) {
                    sender.sendMessage(new TextComponentString("There is no new version available"));
                    return;
                }
                VersionChecker.Version newVersion = VampirismIntegrationsMod.instance.getVersionInfo().getNewVersion();
                List<String> changes = newVersion.getChanges();
                sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Vampirism " + newVersion.name + "(" + MinecraftForge.MC_VERSION + ")"));
                for (String c : changes) {
                    sender.sendMessage(new TextComponentString("-" + c));
                }
                sender.sendMessage(new TextComponentString(""));
                String template = UtilLib.translate("text.vampirism.update_message");
                String homepage = VampirismIntegrationsMod.instance.getVersionInfo().getHomePage();
                template = template.replaceAll("@download@", newVersion.getUrl() == null ? homepage : newVersion.getUrl()).replaceAll("@forum@", homepage);
                ITextComponent component = ITextComponent.Serializer.jsonToComponent(template);
                sender.sendMessage(component);
            }

            @Override
            public String getName() {
                return "changelog";
            }

            @Override
            public String getUsage(ICommandSender sender) {
                return getName();
            }
        });
    }

    @Override
    public String getName() {
        return "vampirism-integrations";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return getName() + " <subcommand>";
    }
}
