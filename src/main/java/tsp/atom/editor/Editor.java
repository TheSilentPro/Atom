package tsp.atom.editor;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tsp.atom.Atom;
import tsp.atom.util.Config;
import tsp.atom.util.Utils;
import tsp.atom.util.XMaterial;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Editor {

    private File file;

    public Editor(File file) {
        this.file = file;
    }

    public List<File> getFiles() {
        if (file.isDirectory()) {
            return Arrays.asList(file.listFiles());
        }
        return null;
    }

    public List<String> getLines() {
        try {
            Scanner scanner = new Scanner(file);
            List<String> lines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }

            return lines;
        } catch (IOException ex) {
            return null;
        }
    }

    public void setLine(int line, String text) throws IOException {
        Path path = Paths.get(file.getPath());
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        lines.set(line, text.replace(Config.getString("spaceCharacter"), " "));
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    public boolean delete() {
        return file.delete();
    }

    public static void open(Player player, File file) {
        if (!file.exists()) {
            Utils.sendMessage(player, "&cThat directory does not exist!");
            return;
        }
        PagedPane main = new PagedPane(6, 6, "Editor");

            for (File f : file.listFiles()) {
                // Create item for file
                ItemStack item = f.isDirectory() ? XMaterial.BOOK.parseItem() : XMaterial.PAPER.parseItem();
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(f.isDirectory() ? ChatColor.YELLOW + f.getName() : ChatColor.AQUA + f.getName());

                // Check if file is hidden
                if (f.isHidden()) meta.addEnchant(Enchantment.DURABILITY, 0, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                // File information
                List<String> lore = new ArrayList<>();
                lore.add(Utils.colorize("&7Type: " + (f.isDirectory() ? "&eDirectory" : "&bFile")));
                if (f.isFile()) lore.add(Utils.colorize("&7Extension: " + getExtension(f)));
                lore.add(Utils.colorize("&7Size: " + f.length() + " B (" + (f.length() / 1000000) + " MB)"));
                lore.add(Utils.colorize("&7R/W: " + (f.canRead() ? "&aY" : "&cN") + "&7/" + (f.canWrite() ? "&aY" : "&cN")));
                lore.add(" ");
                lore.add(Utils.colorize("&eLeft-Click to view contents"));
                lore.add(Utils.colorize("&bRight-Click to edit"));
                meta.setLore(lore);

                item.setItemMeta(meta);

                main.addButton(new Button(item, e -> {
                    Player who = (Player) e.getWhoClicked();

                    if (e.getClick() == ClickType.LEFT) {
                        Bukkit.dispatchCommand(who, (f.isDirectory() ? "vd " : "vf ") + f.getAbsolutePath().replace(" ", "|"));
                    }
                    if (e.getClick() == ClickType.RIGHT) {
                        edit(who, f);
                    }
                }));
            }

        main.open(player);
    }

    public static void edit(Player player, File file) {
        PagedPane edit = new PagedPane(6, 6, "Edit: " + file.getName());

        HashMap<ItemStack, Option> options = new HashMap();
        // Rename
        ItemStack rename = XMaterial.PAPER.parseItem();
        ItemMeta renameMeta = rename.getItemMeta();
        renameMeta.setDisplayName(Utils.colorize("&bRename"));
        rename.setItemMeta(renameMeta);
        options.put(rename, Option.RENAME);
        // Delete
        ItemStack delete = XMaterial.LAVA_BUCKET.parseItem();
        ItemMeta deleteMeta = delete.getItemMeta();
        deleteMeta.setDisplayName(Utils.colorize("&cDelete"));
        delete.setItemMeta(deleteMeta);
        options.put(delete, Option.DELETE);
        // Move
        ItemStack move = XMaterial.DIAMOND_BOOTS.parseItem();
        ItemMeta moveMeta = move.getItemMeta();
        moveMeta.setDisplayName(Utils.colorize("&eMove"));
        move.setItemMeta(moveMeta);
        options.put(move, Option.MOVE);

        for (ItemStack item : options.keySet()) {
            edit.addButton(new Button(item, e -> {
                Player who = (Player) e.getWhoClicked();

                if (e.getClick() == ClickType.LEFT) {

                    switch (options.get(item)) {
                        case DELETE:
                            if (file.delete()) {
                                Utils.sendMessage(who, "File was &cdeleted&7!");
                                who.closeInventory();
                            }else {
                                Utils.sendMessage(who, "&cFailed to delete file!");
                            }
                            break;
                        case RENAME:
                            new AnvilGUI.Builder()
                                    .onComplete((p, text) -> {
                                        if (file.renameTo(new File(text))) {
                                            Utils.sendMessage(p, "File renamed to &e" + text);
                                            return AnvilGUI.Response.close();
                                        }else {
                                            Utils.sendMessage(p, "&cFailed to rename file.");
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
                                            Utils.sendMessage(p, "File moved to &e" + text);
                                            return AnvilGUI.Response.close();
                                        } catch (IOException ioException) {
                                            Utils.sendMessage(p, "&cFailed to move file.");
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

    private static String getExtension(File file) {
        String name = file.getName();
        if(name.lastIndexOf(".") != -1 && name.lastIndexOf(".") != 0)
            return name.substring(name.lastIndexOf(".") + 1);
        else
            return "N/A";
    }

}
