package tsp.atom.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tsp.atom.editor.Editor;
import tsp.atom.util.Config;
import tsp.atom.util.Utils;

import java.io.File;
import java.util.List;

public class Command_viewdir implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!Utils.isAdmin(sender)) {
            Utils.sendMessage(sender, "&cNo permission!");
            return true;
        }
        if (args.length < 1) {
            Utils.sendMessage(sender, "&c/viewdir <path>");
            return true;
        }
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg);
        }
        String path = builder.toString();

        Utils.sendMessage(sender, "Searching for &e" + path.replace(Config.getString("spaceCharacter"), " "));

        File file = new File(path.replace(Config.getString("spaceCharacter"), " "));
        if (!file.exists()) {
            Utils.sendMessage(sender, "&cThat file does not exist!");
            return true;
        }
        if (!file.isDirectory()) {
            Utils.sendMessage(sender, "&cThat file is not a directory! Use /viewfile to view a file.");
            return true;
        }
        if (!file.canRead()) {
            Utils.sendMessage(sender, "&cThat file can not be read!");
            return true;
        }

        Editor editor = new Editor(file);
        List<File> files = editor.getFiles();

        Utils.sendMessage(sender, " ");
        for (int i = 0; i < files.size(); i++) {
            Utils.sendMessage(sender, "&e&l(" + i + ") &f" + files.get(i).getName());
        }
        Utils.sendMessage(sender, " ");
        return true;
    }

}
