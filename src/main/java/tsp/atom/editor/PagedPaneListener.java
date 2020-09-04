package tsp.atom.editor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * Listens for click events for the {@link PagedPane}
 */
public class PagedPaneListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof PagedPane) {
            ((PagedPane) holder).onClick(event);
        }
    }
}
