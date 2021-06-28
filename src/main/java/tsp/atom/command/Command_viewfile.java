package tsp.atom.command;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tsp.atom.editor.Editor;
import tsp.atom.util.Config;
import tsp.atom.util.Utils;

import java.io.File;
import java.util.List;

public class Command_viewfile implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!Utils.isAdmin(sender, "viewfile")) {
            Utils.sendMessage(sender, "&cNo permission!");
            return true;
        }

        if (args.length < 1) {
            Utils.sendMessage(sender, "&c/viewfile <file>");
            Utils.sendMessage(sender, "Remember to use the full &cPATH &7and add the &c.EXTENSION&7!");
            return true;
        }
        String message = Utils.joinArgs(args);

        Utils.sendMessage(sender, "Searching for &e" + message);

        File file = new File(message);
        if (!file.exists()) {
            Utils.sendMessage(sender, "&cThat file does not exist!");
            return true;
        }
        if (file.isDirectory()) {
            Utils.sendMessage(sender, "&cThat path leads to a directory! Use /viewdir <dir> to view directories.");
            return true;
        }
        if (!file.canRead()) {
            Utils.sendMessage(sender, "&cThat file can not be read!");
            return true;
        }

        Editor editor = new Editor(file);
        List<String> lines = editor.getLines();

        Utils.sendMessage(sender, " ");
        for (int i = 0; i < lines.size(); i++) {
            TextComponent component = new TextComponent(Utils.transform("&b(" + i + ") &f" + lines.get(i)));
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Utils.colorize("&7Click to manage line &b" + i + "&7 in chat"))));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ef " + i + " \"" + message + "\" "));
            sender.spigot().sendMessage(component);
        }
        Utils.sendMessage(sender, " ");
        return true;
    }

}
