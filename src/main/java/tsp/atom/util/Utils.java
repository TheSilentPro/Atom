package tsp.atom.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import tsp.atom.Atom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static final Pattern quotePattern = Pattern.compile("\"([^\"]*)\"");

    public static List<String> getTexts(String args) {
        List<String> texts = new ArrayList<>();
        Matcher m = quotePattern.matcher(args);
        while (m.find()) {
            texts.add(m.group().substring(1, m.group().length() - 1));
        }

        return texts;
    }

    public static String joinArgs(String[] args) {
        return joinArgs(0, args);
    }

    public static String joinArgs(int start, String[] args) {
        return String.join(" ", Arrays.copyOfRange(args, start, args.length));
    }

    public static boolean isAdmin(CommandSender sender, String command) {
        if (sender instanceof ConsoleCommandSender) return true;
        if (Atom.getInstance().getConfig().getString("permissionType").equalsIgnoreCase("LIST")) {
            return Atom.getInstance().getConfig().getStringList("admins").contains(sender.getName());
        }

        if (command != null) {
            return sender.hasPermission("atom.admin") || sender.hasPermission("atom.command." + command);
        }

        return sender.hasPermission("atom.admin");
    }

    public static String transform(String message) {
        return colorize("&7[&9&lAtom&7] " + message);
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
