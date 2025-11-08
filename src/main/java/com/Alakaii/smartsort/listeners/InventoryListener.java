package com.Alakaii.smartsort.listeners;

import com.Alakaii.smartsort.SmartSortPlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryListener implements Listener {
    
    private final SmartSortPlugin plugin;
    
    public InventoryListener(SmartSortPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        
        // Check if player is shift+right-clicking on an empty slot
        if (!event.isShiftClick() || !event.isRightClick()) {
            return;
        }
        
        // Must click on empty slot
        if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
            return;
        }
        
        // Check if world is disabled
        if (plugin.getConfigManager().isWorldDisabled(player.getWorld().getName())) {
            plugin.getMessageUtil().sendMessage(player, "sorting-disabled-world");
            return;
        }
        
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }
        
        // Determine if this is a player inventory or container
        boolean isPlayerInventory = clickedInventory.getType() == InventoryType.PLAYER || 
                                    clickedInventory.getType() == InventoryType.CRAFTING;
        
        // Check permissions based on inventory type
        if (isPlayerInventory) {
            if (!hasInventoryPermission(player)) {
                plugin.getMessageUtil().sendMessage(player, "no-permission-inventory");
                event.setCancelled(true);
                return;
            }
        } else {
            // Container sorting - check container-specific permissions
            if (!hasContainerPermission(player, clickedInventory)) {
                plugin.getMessageUtil().sendMessage(player, "no-permission-container");
                event.setCancelled(true);
                return;
            }
        }
        
        // Proceed with sorting
        event.setCancelled(true); // Prevent the click from doing anything else
        
        boolean success;
        
        // For player inventory, determine if hotbar or main inventory was clicked
        if (isPlayerInventory) {
            int slot = event.getSlot();
            
            // Hotbar is slots 0-8, main inventory is slots 9-35 (in player inventory view)
            // When viewing player inventory, the raw slot numbers are different
            boolean isHotbar = slot >= 0 && slot <= 8;
            
            success = plugin.getSortingManager().sortPlayerInventory(player, clickedInventory, isHotbar);
        } else {
            // Container sorting - sort the entire container
            success = plugin.getSortingManager().sortInventory(player, clickedInventory);
        }
        
        if (success) {
            plugin.getMessageUtil().sendMessage(player, "sorted-successfully");
        } else {
            plugin.getMessageUtil().sendMessage(player, "sorting-failed");
        }
    }
    
    /**
     * Check if player has permission to sort their inventory
     * Permission hierarchy:
     * 1. smartsort.use (legacy, grants everything)
     * 2. smartsort.inventory.sort (specific inventory permission)
     */
    private boolean hasInventoryPermission(Player player) {
        // Check legacy permission first (backward compatibility)
        if (player.hasPermission("smartsort.use")) {
            return true;
        }
        
        // Check specific inventory permission
        return player.hasPermission("smartsort.inventory.sort");
    }
    
    /**
     * Check if player has permission to sort the specific container type
     * Permission hierarchy:
     * 1. smartsort.use (legacy, grants everything)
     * 2. smartsort.containers.sort (grants all container types)
     * 3. smartsort.containers.<type> (specific container type)
     */
    private boolean hasContainerPermission(Player player, Inventory inventory) {
        // Check legacy permission first (backward compatibility)
        if (player.hasPermission("smartsort.use")) {
            return true;
        }
        
        // Check general container permission
        if (player.hasPermission("smartsort.containers.sort")) {
            return true;
        }
        
        // Get the container type
        InventoryType type = inventory.getType();
        String containerPermission = getContainerPermission(type, inventory.getHolder());
        
        if (containerPermission != null) {
            return player.hasPermission(containerPermission);
        }
        
        // If no specific permission found, deny by default
        return false;
    }
    
    /**
     * Get the specific permission node for a container type
     */
    private String getContainerPermission(InventoryType type, InventoryHolder holder) {
        switch (type) {
            case CHEST:
                // Check if it's a trapped chest by examining the block
                if (holder instanceof Block) {
                    Block block = (Block) holder;
                    if (block.getType() == Material.TRAPPED_CHEST) {
                        return "smartsort.containers.chest"; // Trapped chests use same permission
                    }
                }
                return "smartsort.containers.chest";
            case BARREL:
                return "smartsort.containers.barrel";
            case SHULKER_BOX:
                return "smartsort.containers.shulkerbox";
            case ENDER_CHEST:
                return "smartsort.containers.enderchest";
            case HOPPER:
                return "smartsort.containers.hopper";
            case DISPENSER:
                return "smartsort.containers.dispenser";
            case DROPPER:
                return "smartsort.containers.dropper";
            default:
                return null;
        }
    }
}
