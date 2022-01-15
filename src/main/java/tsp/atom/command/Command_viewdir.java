package tsp.atom.command;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tsp.atom.editor.Editor;
import tsp.atom.util.Utils;
import tsp.smartplugin.player.PlayerUtils;

import java.io.File;
import java.util.List;

public class Command_viewdir implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!Utils.isAdmin(sender, "viewdir")) {
            PlayerUtils.sendMessage(sender, "&cNo permission!");
            return true;
        }
        if (args.length < 1) {
            PlayerUtils.sendMessage(sender, "&c/viewdir <path>");
            return true;
        }
        String path = Utils.joinArgs(args);

        PlayerUtils.sendMessage(sender, "&7Searching for &e" + path);

        File file = new File(path);
        if (!file.exists()) {
            PlayerUtils.sendMessage(sender, "&cThat file does not exist!");
            return true;
        }
        if (!file.isDirectory()) {
            PlayerUtils.sendMessage(sender, "&cThat file is not a directory! Use /viewfile to view a file.");
            return true;
        }
        if (!file.canRead()) {
            PlayerUtils.sendMessage(sender, "&cThat file can not be read!");
            return true;
        }

        List<File> files = Editor.getFiles(file);

        PlayerUtils.sendMessage(sender, " ");
        for (int i = 0; i < files.size(); i++) {
            String name = files.get(i).getName();
            TextComponent component = new TextComponent(Utils.transform("&e&l(" + i + ") &f" + name));
            if (files.get(i).isDirectory()) {
                component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/editor " + files.get(i).getPath()));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Utils.colorize("&7Click to open the editor for &e" + name))));
            } else {
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Utils.colorize(
                        "&7Size: " + files.get(i).length() + " B (" + (files.get(i).length() / 1_000_000) + " MB) \n" +
                        "&7R/W: " + (files.get(i).canRead() ? "&aY" : "&cN") + "&7/" + (files.get(i).canWrite() ? "&aY" : "&cN"
                )))));
            }
            sender.spigot().sendMessage(component);
        }
        PlayerUtils.sendMessage(sender, " ");
        return true;
    }

}
