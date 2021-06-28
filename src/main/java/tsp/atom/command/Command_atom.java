package tsp.atom.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tsp.atom.Atom;
import tsp.atom.util.Utils;

public class Command_atom implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Atom instance = Atom.getInstance();
        Utils.sendMessage(sender, "Running &9Atom - " + instance.getDescription().getVersion());
        Utils.sendMessage(sender, "Created by &9" + instance.getDescription().getAuthors());
        return true;
    }
}
