package tsp.atom;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tsp.atom.command.*;
import tsp.atom.editor.PagedPaneListener;
import tsp.atom.util.Log;
import tsp.atom.util.Metrics;
import tsp.atom.util.Utils;

public class Atom extends JavaPlugin {

    private static Atom instance;

    @Override
    public void onEnable() {
        Log.info("Loading Atom - " + getDescription().getVersion());
        saveDefaultConfig();
        instance = this;

        Log.debug("Registering commands...");
        getCommand("atom").setExecutor(new Command_atom());
        getCommand("editor").setExecutor(new Command_editor());
        getCommand("viewfile").setExecutor(new Command_viewfile());
        getCommand("editfile").setExecutor(new Command_editfile());
        getCommand("viewdir").setExecutor(new Command_viewdir());
        getCommand("deletefile").setExecutor(new Command_deletefile());

        Bukkit.getPluginManager().registerEvents(new PagedPaneListener(), this);

        Log.debug("Starting metrics...");
        new Metrics(this, Utils.METRICS_ID);

        Log.info("Done!");
        Log.info(" ");
        Log.info("Running &9Atom v" + getDescription().getVersion() + " &aby &9" + getDescription().getAuthors());
        Log.info(" ");
    }

    public static Atom getInstance() {
        return instance;
    }

}
