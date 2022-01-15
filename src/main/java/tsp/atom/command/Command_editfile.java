package tsp.atom.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tsp.atom.editor.Editor;
import tsp.atom.util.Utils;
import tsp.smartplugin.player.PlayerUtils;
import tsp.smartplugin.server.Log;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Command_editfile implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!Utils.isAdmin(sender, "editfile")) {
            PlayerUtils.sendMessage(sender, "&cNo permission!");
            return true;
        }

        if (args.length < 1) {
            PlayerUtils.sendMessage(sender, "&c/editfile <line> \"<file>\" \"<text>\"");
            PlayerUtils.sendMessage(sender, "&7Remember to use the full &cPATH &7and add the &c.EXTENSION&7!");
            return true;
        }

        int line;
        try {
            line = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            PlayerUtils.sendMessage(sender, "&cLine is not a number!");
            return true;
        }
        List<String> arguments = Utils.getTexts(Utils.joinArgs(1, args));
        if (arguments.size() < 1) {
            PlayerUtils.sendMessage(sender, "&cInvalid arguments!");
            return true;
        }

        String path = arguments.get(0);
        String text = arguments.get(1);

        PlayerUtils.sendMessage(sender, "&7Searching for &e" + path);

        File file = new File(path);
        if (!file.exists()) {
            PlayerUtils.sendMessage(sender, "&cThat file does not exist!");
            return true;
        }
        if (!file.canRead()) {
            PlayerUtils.sendMessage(sender, "&cThat file can not be read!");
            return true;
        }

        try {
            Editor.setLine(line, text, file);
            PlayerUtils.sendMessage(sender, "&7Line &9" + line + " &7was set to &e" + text);
        } catch (IOException e) {
            PlayerUtils.sendMessage(sender, "&cSomething went wrong! Check console...");
            Log.error("CATCH -> IOException | Message: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

}
