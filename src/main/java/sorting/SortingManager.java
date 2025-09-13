package com.Alakaii.smartsort.sorting;

import com.Alakaii.smartsort.SmartSortPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SortingManager {

    private final SmartSortPlugin plugin;
    private final Map<String, Long> lastSortTime = new ConcurrentHashMap<>();
    private final ItemCategorizer categorizer;

    public SortingManager(SmartSortPlugin plugin) {
        this.plugin = plugin;
        this.categorizer = new ItemCategorizer();
    }

    public boolean sortInventory(Inventory inventory, Player player) {
        // Rate limiting check
        if (!canSort(player)) {
            return false;
        }

        try {
            // Record sort time for rate limiting
            lastSortTime.put(player.getUniqueId().toString(), System.currentTimeMillis());

            // Get sorting range based on inventory type
            int[] sortingRange = getSortingRange(inventory);
            int startSlot = sortingRange[0];
            int endSlot = sortingRange[1];

            // Extract items from the sorting range
            List<ItemStack> items = new ArrayList<>();
            for (int i = startSlot; i <= endSlot; i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null && item.getType() != Material.AIR) {
                    items.add(item.clone());
                } else {
                    items.add(null);
                }
            }

            // Stack similar items if enabled
            if (plugin.getConfigManager().shouldStackItems()) {
                items = stackItems(items);
            }

            // Sort the items
            List<ItemStack> sortedItems = sortItems(items);

            // Clear the sorting range
            for (int i = startSlot; i <= endSlot; i++) {
                inventory.setItem(i, null);
            }

            // Place sorted items back
            int slot = startSlot;
            for (ItemStack item : sortedItems) {
                if (item != null && item.getType() != Material.AIR && slot <= endSlot) {
                    inventory.setItem(slot, item);
                    slot++;
                }
            }

            return true;

        } catch (Exception e) {
            plugin.getLogger().warning("Error sorting inventory for player " + player.getName() + ": " + e.getMessage());
            if (plugin.getConfigManager().isVerbose()) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private boolean canSort(Player player) {
        String playerId = player.getUniqueId().toString();
        long currentTime = System.currentTimeMillis();

        if (lastSortTime.containsKey(playerId)) {
            long lastSort = lastSortTime.get(playerId);
            long timeDiff = currentTime - lastSort;
            long minInterval = 1000 / plugin.getConfigManager().getMaxSortingOperationsPerSecond();

            return timeDiff >= minInterval;
        }

        return true;
    }

    private int[] getSortingRange(Inventory inventory) {
        if (inventory instanceof PlayerInventory) {
            // Player inventory: slots 9-35 (main inventory, not hotbar)
            return new int[]{9, 35};
        } else {
            // Container inventory: all slots
            return new int[]{0, inventory.getSize() - 1};
        }
    }

    private List<ItemStack> stackItems(List<ItemStack> items) {
        Map<String, ItemStack> stackedItems = new HashMap<>();

        for (ItemStack item : items) {
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            String key = getItemKey(item);
            if (stackedItems.containsKey(key)) {
                ItemStack existing = stackedItems.get(key);
                int maxStack = existing.getMaxStackSize();
                int currentAmount = existing.getAmount();
                int newAmount = item.getAmount();

                if (currentAmount + newAmount <= maxStack) {
                    existing.setAmount(currentAmount + newAmount);
                } else {
                    // Create additional stacks if needed
                    existing.setAmount(maxStack);
                    ItemStack overflow = item.clone();
                    overflow.setAmount((currentAmount + newAmount) - maxStack);

                    // Find a new key for overflow
                    int counter = 1;
                    String overflowKey = key + "_overflow_" + counter;
                    while (stackedItems.containsKey(overflowKey)) {
                        counter++;
                        overflowKey = key + "_overflow_" + counter;
                    }
                    stackedItems.put(overflowKey, overflow);
                }
            } else {
                stackedItems.put(key, item.clone());
            }
        }

        return new ArrayList<>(stackedItems.values());
    }

    private List<ItemStack> sortItems(List<ItemStack> items) {
        // Filter out null/air items
        List<ItemStack> validItems = items.stream()
                .filter(item -> item != null && item.getType() != Material.AIR)
                .collect(ArrayList::new, (list, item) -> list.add(item.clone()), ArrayList::addAll);

        // Sort by category first, then alphabetically if enabled
        validItems.sort((item1, item2) -> {
            String category1 = categorizer.getCategory(item1.getType());
            String category2 = categorizer.getCategory(item2.getType());

            // Compare by category priority
            int categoryComparison = Integer.compare(
                    categorizer.getCategoryPriority(category1),
                    categorizer.getCategoryPriority(category2)
            );

            if (categoryComparison != 0) {
                return categoryComparison;
            }

            // If same category and alphabetical sorting is enabled
            if (plugin.getConfigManager().isAlphabeticalSorting()) {
                return item1.getType().name().compareTo(item2.getType().name());
            }

            return 0;
        });

        return validItems;
    }

    private String getItemKey(ItemStack item) {
        StringBuilder key = new StringBuilder();
        key.append(item.getType().name());

        if (item.hasItemMeta()) {
            if (item.getItemMeta().hasDisplayName()) {
                key.append("_").append(item.getItemMeta().getDisplayName());
            }
            if (item.getItemMeta().hasEnchants()) {
                key.append("_enchanted");
            }
        }

        return key.toString();
    }

    public boolean hasContainerAccess(Player player, Inventory inventory) {
        // Basic implementation - can be expanded for protection plugin integration
        if (!plugin.getConfigManager().isProtectionIntegrationEnabled()) {
            return true;
        }

        // TODO: Add integration with WorldGuard, LWC, etc.
        // For now, assume player has access
        return true;
    }
}