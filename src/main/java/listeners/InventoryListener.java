package com.Alakaii.smartsort.listeners;

import com.Alakaii.smartsort.SmartSortPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    private final SmartSortPlugin plugin;

    public InventoryListener(SmartSortPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        // Only handle player clicks
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        // Check if it's Shift+Right-Click
        if (event.getClick() != ClickType.SHIFT_RIGHT) {
            return;
        }

        // Check if clicked on empty slot
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem != null && clickedItem.getType() != Material.AIR) {
            return;
        }

        // Check permission
        if (!player.hasPermission("smartsort.use")) {
            plugin.getMessageUtil().sendMessage(player, "no-permission");
            return;
        }

        // Check if player has SmartSort enabled
        if (!plugin.getConfigManager().isPlayerEnabled(player)) {
            return; // Silently ignore if disabled
        }

        // Check if world is disabled
        String worldName = player.getWorld().getName();
        if (plugin.getConfigManager().isWorldDisabled(worldName)) {
            plugin.getMessageUtil().sendMessage(player, "sorting-disabled-world");
            return;
        }

        // Determine which inventory to sort
        Inventory targetInventory = null;

        // Check if clicked in player inventory
        if (event.getClickedInventory() != null &&
                event.getClickedInventory().getType() == InventoryType.PLAYER) {
            targetInventory = player.getInventory();
        }
        // Check if clicked in a container
        else if (event.getClickedInventory() != null &&
                isAllowedContainer(event.getClickedInventory().getType())) {
            targetInventory = event.getClickedInventory();
        }

        // If no valid inventory found, return
        if (targetInventory == null) {
            return;
        }

        // Check if player has access to the container (for protection plugins)
        if (targetInventory != player.getInventory() &&
                !plugin.getSortingManager().hasContainerAccess(player, targetInventory)) {
            return;
        }

        // Cancel the click event to prevent any item movement
        event.setCancelled(true);

        // Perform the sorting
        boolean success = plugin.getSortingManager().sortInventory(targetInventory, player);

        if (success) {
            plugin.getMessageUtil().sendMessage(player, "sorted-successfully");
            if (plugin.getConfigManager().isVerbose()) {
                plugin.getLogger().info("Player " + player.getName() + " sorted inventory in world " + worldName);
            }
        } else {
            plugin.getMessageUtil().sendMessage(player, "sorting-failed");
        }
    }

    private boolean isAllowedContainer(InventoryType type) {
        return plugin.getConfigManager().getAllowedContainers().contains(type.name());
    }
}