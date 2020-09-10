package tsp.atom.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class Utils {

    public static final String PREFIX = "&7[&9&lAtom&7] ";
    public static final int METRICS_ID = 8813;

    public static boolean isAdmin(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) return true;
        if (Config.getString("permissionType").equalsIgnoreCase("LIST")) {
            return Config.getStringList("admins").contains(sender.getName());
        }
        return sender.hasPermission("atom.admin");
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(colorize(PREFIX + message));
    }

    public static String transform(String message) {
        return colorize(PREFIX + message);
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
