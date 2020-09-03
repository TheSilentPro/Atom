package tsp.atom.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tsp.atom.editor.Editor;
import tsp.atom.util.Config;
import tsp.atom.util.Log;
import tsp.atom.util.Utils;

import java.io.File;
import java.io.IOException;

public class Command_editfile implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!Utils.isAdmin(sender)) {
            Utils.sendMessage(sender, "&cNo permission!");
            return true;
        }

        if (args.length < 1) {
            Utils.sendMessage(sender, "&c/editfile <line> <file> <text>");
            Utils.sendMessage(sender, "Remember to use the full &cPATH &7and add the &c.EXTENSION&7!");
            return true;
        }

        int line = -1;
        try {
            line = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            Utils.sendMessage(sender, "&cLine is not a number!");
            return true;
        }
        if (line == -1) {
            Utils.sendMessage(sender, "&cLine is not a number!");
            return true;
        }

        String path = args[1];


        StringBuilder builder = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }
        String text = builder.toString().replace(Config.getString("spaceCharacter"), " ");

        Utils.sendMessage(sender, "Searching for &e" + path.replace(Config.getString("spaceCharacter"), " "));

        File file = new File(path.replace(Config.getString("spaceCharacter"), " "));
        if (!file.exists()) {
            Utils.sendMessage(sender, "&cThat file does not exist!");
            return true;
        }
        if (!file.canRead()) {
            Utils.sendMessage(sender, "&cThat file can not be read!");
            return true;
        }

        Editor editor = new Editor(file);
        try {
            editor.setLine(line, text);
            Utils.sendMessage(sender, "Line &9" + line + " &7was set to &e" + text);
        } catch (IOException e) {
            Utils.sendMessage(sender, "&cSomething went wrong! Check console...");
            Log.error("CATCH -> IOException | Message: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

}
