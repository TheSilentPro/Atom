package tsp.atom.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Config {

    private static File configFile = new File("plugins/Atom/config.yml");
    private static FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

    public static String getString(String path) {
        return Utils.colorize(config.getString(path));
    }

    public static boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public static List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public static FileConfiguration getConfig() {
        return config;
    }

}
