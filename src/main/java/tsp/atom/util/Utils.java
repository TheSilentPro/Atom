package tsp.atom.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class Utils {

    public static boolean isAdmin(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) return true;
        if (Config.getString("permissionType").equalsIgnoreCase("LIST")) {
            return Config.getStringList("admins").contains(sender.getName());
        }
        return sender.hasPermission("atom.admin");
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(colorize("&7[&9&lAtom" + "&7] " + message));
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
