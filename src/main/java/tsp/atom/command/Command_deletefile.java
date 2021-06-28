package tsp.atom.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tsp.atom.editor.Editor;
import tsp.atom.util.Config;
import tsp.atom.util.Utils;

import java.io.File;

public class Command_deletefile implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!Utils.isAdmin(sender, "deletefile")) {
            Utils.sendMessage(sender, "&cNo permission!");
            return true;
        }
        if (args.length < 1) {
            Utils.sendMessage(sender, "&c/deletefile <file>");
            return true;
        }
        String path = Utils.joinArgs(args);

        Utils.sendMessage(sender, "Searching for &e" + path);

        File file = new File(path);
        if (!file.exists()) {
            Utils.sendMessage(sender, "&cThat file does not exist!");
            return true;
        }

        Editor editor = new Editor(file);
        if (editor.delete()) {
            Utils.sendMessage(sender, "File was &cdeleted&7!");
        }else {
            Utils.sendMessage(sender, "&cFailed to delete file!");
        }
        return true;
    }

}
