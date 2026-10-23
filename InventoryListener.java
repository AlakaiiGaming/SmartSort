package com.Alakaii.smartsort.listeners;

import com.Alakaii.smartsort.SmartSortPlugin;
import com.Alakaii.smartsort.sorting.SortingManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    private final SmartSortPlugin plugin;

    public InventoryListener(SmartSortPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        // Only Shift + Right-Click on an empty slot with nothing on the cursor
        if (event.getClick() != ClickType.SHIFT_RIGHT) {
            return;
        }
        ItemStack current = event.getCurrentItem();
        if (current != null && !current.isEmpty()) {
            return;
        }
        if (!event.getCursor().isEmpty()) {
            return;
        }

        Inventory clicked = event.getClickedInventory();
        if (clicked == null) {
            return;
        }

        SortingManager sorter = plugin.getSortingManager();
        boolean isPlayerInventory = clicked.getType() == InventoryType.PLAYER
                && clicked.equals(player.getInventory());

        Boolean hotbar = null;   // only used for player inventory
        if (isPlayerInventory) {
            int slot = event.getSlot();
            if (slot >= 0 && slot <= 8) {
                hotbar = true;
            } else if (slot >= 9 && slot <= 35) {
                hotbar = false;
            } else {
                return; // armor / offhand slots: ignore
            }
        } else if (sorter.getContainerKey(player, clicked) == null) {
            return; // furnaces, anvils, other plugins' menus, disabled container types...
        }

        // From here on this click is ours
        event.setCancelled(true);

        if (plugin.getConfigManager().isWorldDisabled(player.getWorld().getName())) {
            plugin.getMessageUtil().sendMessage(player, "sorting-disabled-world");
            return;
        }

        if (isPlayerInventory) {
            if (!sorter.canSortPlayerInventory(player)) {
                plugin.getMessageUtil().sendMessage(player, "no-permission-inventory");
                return;
            }
        } else if (!sorter.canSortContainer(player, sorter.getContainerKey(player, clicked))) {
            plugin.getMessageUtil().sendMessage(player, "no-permission-container");
            return;
        }

        if (!sorter.tryAcquireCooldown(player)) {
            return;
        }

        // Sort one tick later so the cancelled click finishes cleanly on the client first
        final boolean sortHotbar = hotbar != null && hotbar;
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            if (!player.isOnline()) {
                return;
            }
            SortingManager.Result result;
            if (isPlayerInventory) {
                result = sorter.sortPlayerInventory(player, player.getInventory(), sortHotbar);
            } else {
                // The container must still be open (it may have been closed or broken)
                if (!player.getOpenInventory().getTopInventory().equals(clicked)) {
                    return;
                }
                result = sorter.sortInventory(player, clicked);
            }
            sendResult(player, result);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getSortingManager().forget(event.getPlayer());
    }

    private void sendResult(Player player, SortingManager.Result result) {
        switch (result) {
            case SORTED -> plugin.getMessageUtil().sendMessage(player, "sorted-successfully");
            case NOTHING_TO_SORT -> plugin.getMessageUtil().sendMessage(player, "nothing-to-sort");
            case FAILED -> plugin.getMessageUtil().sendMessage(player, "sorting-failed");
        }
    }
}
