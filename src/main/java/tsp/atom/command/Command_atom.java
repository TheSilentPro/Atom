package tsp.atom.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tsp.atom.Atom;
import tsp.smartplugin.player.PlayerUtils;

public class Command_atom implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Atom instance = Atom.getInstance();
        PlayerUtils.sendMessage(sender, "&7Running &9Atom - " + instance.getDescription().getVersion());
        PlayerUtils.sendMessage(sender, "&7Created by &9" + instance.getDescription().getAuthors());
        return true;
    }
}
