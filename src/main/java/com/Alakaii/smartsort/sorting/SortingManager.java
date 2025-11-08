package com.Alakaii.smartsort.sorting;

import com.Alakaii.smartsort.SmartSortPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SortingManager {
    
    private final SmartSortPlugin plugin;
    
    public SortingManager(SmartSortPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Sort player inventory with separate hotbar and main inventory sections
     * @param player The player whose inventory is being sorted
     * @param inventory The inventory to sort
     * @param isHotbar True if sorting hotbar, false if sorting main inventory
     * @return true if successful
     */
    public boolean sortPlayerInventory(Player player, Inventory inventory, boolean isHotbar) {
        try {
            List<ItemStack> items = new ArrayList<>();
            
            // Determine which slots to sort
            int startSlot;
            int endSlot;
            
            if (isHotbar) {
                // Hotbar: slots 0-8
                startSlot = 0;
                endSlot = 8;
            } else {
                // Main inventory: slots 9-35 (27 slots)
                startSlot = 9;
                endSlot = 35;
            }
            
            // Collect items from the specified range
            for (int i = startSlot; i <= endSlot; i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null && item.getType() != Material.AIR) {
                    items.add(item.clone());
                }
            }
            
            if (items.isEmpty()) {
                return false;
            }
            
            // Stack similar items if configured
            if (plugin.getConfigManager().shouldStackItems()) {
                items = stackSimilarItems(items);
            }
            
            // Sort items
            items = sortItems(items);
            
            // Clear only the range we're sorting
            for (int i = startSlot; i <= endSlot; i++) {
                inventory.setItem(i, null);
            }
            
            // Place sorted items back into the same range
            int slot = startSlot;
            for (ItemStack item : items) {
                if (slot > endSlot) {
                    break;
                }
                inventory.setItem(slot++, item);
            }
            
            return true;
            
        } catch (Exception e) {
            plugin.getLogger().severe("Error sorting player inventory: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean sortInventory(Player player, Inventory inventory) {
        try {
            // Collect all items from the inventory
            List<ItemStack> items = new ArrayList<>();
            
            for (ItemStack item : inventory.getContents()) {
                if (item != null && item.getType() != Material.AIR) {
                    items.add(item.clone());
                }
            }
            
            if (items.isEmpty()) {
                return false;
            }
            
            // Stack similar items if configured
            if (plugin.getConfigManager().shouldStackItems()) {
                items = stackSimilarItems(items);
            }
            
            // Sort items
            items = sortItems(items);
            
            // Clear the inventory
            inventory.clear();
            
            // Place sorted items back
            int slot = 0;
            for (ItemStack item : items) {
                if (slot >= inventory.getSize()) {
                    break;
                }
                inventory.setItem(slot++, item);
            }
            
            return true;
            
        } catch (Exception e) {
            plugin.getLogger().severe("Error sorting inventory: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private List<ItemStack> stackSimilarItems(List<ItemStack> items) {
        Map<String, ItemStack> stacked = new HashMap<>();
        
        for (ItemStack item : items) {
            String key = getItemKey(item);
            
            if (stacked.containsKey(key)) {
                ItemStack existing = stacked.get(key);
                int maxStack = existing.getType().getMaxStackSize();
                int currentAmount = existing.getAmount();
                int itemAmount = item.getAmount();
                int totalAmount = currentAmount + itemAmount;
                
                if (totalAmount <= maxStack) {
                    existing.setAmount(totalAmount);
                } else {
                    existing.setAmount(maxStack);
                    item.setAmount(totalAmount - maxStack);
                    stacked.put(key + "_" + UUID.randomUUID(), item);
                }
            } else {
                stacked.put(key, item);
            }
        }
        
        return new ArrayList<>(stacked.values());
    }
    
    private String getItemKey(ItemStack item) {
        StringBuilder key = new StringBuilder();
        key.append(item.getType().name());
        
        if (item.hasItemMeta()) {
            key.append("_").append(item.getItemMeta().hashCode());
        }
        
        return key.toString();
    }
    
    private List<ItemStack> sortItems(List<ItemStack> items) {
        ItemCategorizer categorizer = plugin.getItemCategorizer();
        boolean alphabetical = plugin.getConfigManager().shouldSortAlphabetically();
        
        items.sort((item1, item2) -> {
            // First, sort by category
            String cat1 = categorizer.getCategory(item1);
            String cat2 = categorizer.getCategory(item2);
            
            int priority1 = categorizer.getCategoryPriority(cat1);
            int priority2 = categorizer.getCategoryPriority(cat2);
            
            if (priority1 != priority2) {
                return Integer.compare(priority1, priority2);
            }
            
            // Within same category, sort alphabetically if configured
            if (alphabetical) {
                return item1.getType().name().compareTo(item2.getType().name());
            }
            
            return 0;
        });
        
        return items;
    }
}
