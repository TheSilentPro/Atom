package tsp.atom.editor;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tsp.atom.Atom;
import tsp.atom.util.Utils;
import tsp.smartplugin.builder.ItemBuilder;
import tsp.smartplugin.inventory.Button;
import tsp.smartplugin.inventory.paged.PagedPane;
import tsp.smartplugin.inventory.single.Pane;
import tsp.smartplugin.player.PlayerUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Editor {

    public static List<File> getFiles(File file) {
        if (file.isDirectory()) {
            return Arrays.asList(file.listFiles());
        }
        return null;
    }

    public static List<String> getLines(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            List<String> lines = new ArrayList<>();
            for (String line; (line = reader.readLine()) != null;) {
                lines.add(line);
            }

            return lines;
        } catch (IOException ex) {
            return null;
        }
    }

    public static void setLine(int line, String text, File file) throws IOException {
        Path path = Paths.get(file.getPath());
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        lines.set(line, text);
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    public static void open(Player player, File file) {
        if (!file.exists()) {
            PlayerUtils.sendMessage(player, "&cThat directory does not exist!");
            return;
        }
        PagedPane main = new PagedPane(6, 6, "Editor");

            for (File f : file.listFiles()) {
                // Create item for file
                ItemStack item = f.isDirectory() ? new ItemStack(Material.CHEST) : new ItemStack(Material.PAPER);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(f.isDirectory() ? ChatColor.YELLOW + f.getName() : ChatColor.AQUA + f.getName());

                // Check if file is hidden
                if (f.isHidden()) meta.addEnchant(Enchantment.DURABILITY, 0, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                // File information
                List<String> lore = new ArrayList<>();
                lore.add(Utils.colorize("&7Type: " + (f.isDirectory() ? "&eDirectory" : "&bFile")));
                if (f.isFile()) lore.add(Utils.colorize("&7Extension: " + getExtension(f)));
                lore.add(Utils.colorize("&7Size: " + (f.length() / 1_000_000) + " MB (" + f.length() + " B)"));
                lore.add(Utils.colorize("&7R/W: " + (f.canRead() ? "&aY" : "&cN") + "&7/" + (f.canWrite() ? "&aY" : "&cN")));
                lore.add(" ");
                lore.add(Utils.colorize("&eLeft-Click to view contents"));
                if (f.isDirectory()) lore.add(Utils.colorize("&bRight-Click to open and manage"));
                lore.add(Utils.colorize("&cShift-Right-Click to manage file"));
                meta.setLore(lore);

                item.setItemMeta(meta);

                main.addButton(new Button(item, e -> {
                    Player who = (Player) e.getWhoClicked();

                    if (e.getClick() == ClickType.LEFT) {
                        Bukkit.dispatchCommand(who, (f.isDirectory() ? "vd " : "vf ") + f.getAbsolutePath());
                    }
                    if (e.getClick() == ClickType.RIGHT && f.isDirectory()) {
                        Bukkit.dispatchCommand(who, "editor " + f.getAbsolutePath());
                    }
                    if (e.getClick() == ClickType.SHIFT_RIGHT) {
                        manage(who, f);
                    }
                }));
            }

        main.open(player);
    }

    public static void manage(Player player, File file) {
        Pane edit = new Pane(6, "Edit: " + file.getName());

        Map<ItemStack, Option> options = new HashMap<>();
        // Rename
        ItemStack rename = new ItemBuilder(Material.NAME_TAG)
                .name("&b&lRENAME")
                .build();
        options.put(rename, Option.RENAME);
        // Delete
        ItemStack delete = new ItemBuilder(Material.LAVA_BUCKET)
                .name("&c&lDELETE")
                .build();
        options.put(delete, Option.DELETE);
        // Move
        ItemStack move = new ItemBuilder(Material.DIAMOND_BOOTS)
                .name("&e&lMOVE")
                .build();
        options.put(move, Option.MOVE);

        for (ItemStack item : options.keySet()) {
            edit.addButton(new Button(item, e -> {
                Player who = (Player) e.getWhoClicked();

                if (e.getClick() == ClickType.LEFT) {

                    switch (options.get(item)) {
                        case DELETE:
                            if (file.delete()) {
                                PlayerUtils.sendMessage(who, "File was &cdeleted&7!");
                                who.closeInventory();
                            }else {
                                PlayerUtils.sendMessage(who, "&cFailed to delete file!");
                            }
                            break;
                        case RENAME:
                            new AnvilGUI.Builder()
                                    .onComplete((p, text) -> {
                                        if (file.renameTo(new File(text))) {
                                            PlayerUtils.sendMessage(p, "File renamed to &e" + text);
                                            return AnvilGUI.Response.close();
                                        }else {
                                            PlayerUtils.sendMessage(p, "&cFailed to rename file.");
                                            return AnvilGUI.Response.text("Failed...");
                                        }
                                    })
                                    .text("Enter a name...")
                                    .title(Utils.colorize("&bRename: " + file.getName()))
                                    .plugin(Atom.getInstance())
                                    .open(who);
                            break;
                        case MOVE:
                            new AnvilGUI.Builder()
                                    .onComplete((p, text) -> {
                                        try {
                                            Path dest = Paths.get(text).endsWith("/") ? Paths.get(text + file.getName()) : Paths.get(text + "/" + file.getName());
                                            Files.move(Paths.get(file.getAbsolutePath()), dest, StandardCopyOption.REPLACE_EXISTING);
                                            PlayerUtils.sendMessage(p, "File moved to &e" + text);
                                            return AnvilGUI.Response.close();
                                        } catch (IOException ioException) {
                                            PlayerUtils.sendMessage(p, "&cFailed to move file.");
                                            return AnvilGUI.Response.text("Failed.");
                                        }
                                    })
                                    .text("Enter a directory...")
                                    .title(Utils.colorize("&eMove: " + file.getName()))
                                    .plugin(Atom.getInstance())
                                    .open(who);
                            break;
                    }
                }
            }));
        }

        edit.open(player);
    }

    public static String getExtension(File file) {
        String name = file.getName();
        if(name.lastIndexOf(".") != -1 && name.lastIndexOf(".") != 0)
            return name.substring(name.lastIndexOf(".") + 1);
        else
            return "N/A";
    }

}
