package tsp.atom.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Log {

    public static void info(String message) {
        log(LogLevel.INFO, message);
    }

    public static void warning(String message) {
        log(LogLevel.WARNING, message);
    }

    public static void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public static void debug(String message) {
        if (Config.getConfig() != null && Config.getBoolean("debug")) {
            log(LogLevel.DEBUG, message);
        }
    }

    public static void log(LogLevel level, String message) {
        Bukkit.getConsoleSender().sendMessage(Utils.colorize("&7[&9Atom&7] " + level.getColor() + "[" + level.name() + "]: " + Utils.colorize(message)));
    }

    public enum LogLevel {

        INFO,
        WARNING,
        ERROR,
        DEBUG;

        private ChatColor getColor() {
            switch (this) {
                case INFO:
                    return ChatColor.GREEN;
                case WARNING:
                    return ChatColor.YELLOW;
                case ERROR:
                    return ChatColor.DARK_RED;
                case DEBUG:
                    return ChatColor.DARK_AQUA;
                default:
                    return ChatColor.WHITE;
            }
        }

    }

}
