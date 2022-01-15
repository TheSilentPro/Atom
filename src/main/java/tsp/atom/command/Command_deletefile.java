package tsp.atom.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tsp.atom.util.Utils;
import tsp.smartplugin.player.PlayerUtils;

import java.io.File;

public class Command_deletefile implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!Utils.isAdmin(sender, "deletefile")) {
            PlayerUtils.sendMessage(sender, "&cNo permission!");
            return true;
        }
        if (args.length < 1) {
            PlayerUtils.sendMessage(sender, "&c/deletefile <file>");
            return true;
        }
        String path = Utils.joinArgs(args);

        PlayerUtils.sendMessage(sender, "&7Searching for &e" + path);

        File file = new File(path);
        if (!file.exists()) {
            PlayerUtils.sendMessage(sender, "&cThat file does not exist!");
            return true;
        }

        if (file.delete()) {
            PlayerUtils.sendMessage(sender, "&7File was &cdeleted&7!");
        }else {
            PlayerUtils.sendMessage(sender, "&cFailed to delete file!");
        }
        return true;
    }

}
