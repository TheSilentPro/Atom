package tsp.atom;

import tsp.atom.command.*;
import tsp.atom.util.Metrics;
import tsp.smartplugin.SmartPlugin;
import tsp.smartplugin.server.Log;

public class Atom extends SmartPlugin {

    private static Atom instance;

    @Override
    public void onStart() {
        instance = this;
        Log.info("Loading Atom - " + getDescription().getVersion());
        saveDefaultConfig();

        Log.debug("Registering commands...");
        getCommand("atom").setExecutor(new Command_atom());
        getCommand("editor").setExecutor(new Command_editor());
        getCommand("viewfile").setExecutor(new Command_viewfile());
        getCommand("editfile").setExecutor(new Command_editfile());
        getCommand("viewdir").setExecutor(new Command_viewdir());
        getCommand("deletefile").setExecutor(new Command_deletefile());

        Log.debug("Starting metrics...");
        new Metrics(this, 8813);

        Log.info("Done!");
    }

    public static Atom getInstance() {
        return instance;
    }

}
