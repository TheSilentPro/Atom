package tsp.atom.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tsp.atom.editor.Editor;
import tsp.atom.util.Utils;
import tsp.smartplugin.player.PlayerUtils;

import java.io.File;

public class Command_editor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!Utils.isAdmin(sender, "editor")) {
            PlayerUtils.sendMessage(sender, "&cNo permission!");
            return true;
        }
        if (!(sender instanceof Player)) {
            PlayerUtils.sendMessage(sender, "&cOnly for in-game players!");
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            Editor.open(player, new File("./"));
            return true;
        }

        Editor.open(player, new File(Utils.joinArgs(args)));
        return true;
    }

}
