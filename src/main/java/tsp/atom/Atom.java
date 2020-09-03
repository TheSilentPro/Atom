package tsp.atom;

import org.bukkit.plugin.java.JavaPlugin;
import tsp.atom.command.Command_atom;
import tsp.atom.command.Command_editfile;
import tsp.atom.command.Command_viewdir;
import tsp.atom.command.Command_viewfile;
import tsp.atom.util.Log;

public class Atom extends JavaPlugin {

    private static Atom instance;

    @Override
    public void onEnable() {
        Log.info("Loading Atom - " + getDescription().getVersion());
        instance = this;

        Log.debug("Loading files...");
        saveDefaultConfig();

        Log.debug("Registering commands...");
        getCommand("atom").setExecutor(new Command_atom());
        getCommand("viewfile").setExecutor(new Command_viewfile());
        getCommand("editfile").setExecutor(new Command_editfile());
        getCommand("viewdir").setExecutor(new Command_viewdir());

        Log.info("Done!");
        Log.info(" ");
        Log.info("Running &9Atom v" + getDescription().getVersion() + " &aby &9" + getDescription().getAuthors());
        Log.info(" ");
    }

    public static Atom getInstance() {
        return instance;
    }

}
