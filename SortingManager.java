package com.Alakaii.smartsort.sorting;

import com.Alakaii.smartsort.SmartSortPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SortingManager {

    public enum Result { SORTED, NOTHING_TO_SORT, FAILED }

    private final SmartSortPlugin plugin;
    private final Map<UUID, Long> lastSort = new HashMap<>();

    public SortingManager(SmartSortPlugin plugin) {
        this.plugin = plugin;
    }

    // ------------------------------------------------------------------
    // Permissions and eligibility
    // ------------------------------------------------------------------

    public boolean canSortPlayerInventory(Player player) {
        if (!plugin.getConfigManager().usePermissions()) {
            return true;
        }
        return player.hasPermission("smartsort.use") || player.hasPermission("smartsort.inventory.sort");
    }

    public boolean canSortContainer(Player player, String containerKey) {
        if (!plugin.getConfigManager().usePermissions()) {
            return true;
        }
        if (player.hasPermission("smartsort.use") || player.hasPermission("smartsort.containers.sort")) {
            return true;
        }
        String node = switch (containerKey) {
            case "CHEST", "TRAPPED_CHEST" -> "smartsort.containers.chest";
            case "BARREL" -> "smartsort.containers.barrel";
            case "SHULKER_BOX" -> "smartsort.containers.shulkerbox";
            case "ENDER_CHEST" -> "smartsort.containers.enderchest";
            case "HOPPER" -> "smartsort.containers.hopper";
            case "DISPENSER" -> "smartsort.containers.dispenser";
            case "DROPPER" -> "smartsort.containers.dropper";
            default -> null;
        };
        return node != null && player.hasPermission(node);
    }

    /**
     * Works out which allowed-containers entry an inventory counts as, or null if it must never
     * be sorted: furnaces, anvils, horse inventories, other plugins' menus (CMI and the like), and
     * anything not listed in allowed-containers.
     */
    public String getContainerKey(Player player, Inventory inventory) {
        InventoryHolder holder = inventory.getHolder(false);

        String key = switch (inventory.getType()) {
            case CHEST -> isTrappedChest(inventory) ? "TRAPPED_CHEST" : "CHEST";
            case BARREL -> "BARREL";
            case SHULKER_BOX -> "SHULKER_BOX";
            case ENDER_CHEST -> "ENDER_CHEST";
            case HOPPER -> "HOPPER";
            case DISPENSER -> "DISPENSER";
            case DROPPER -> "DROPPER";
            default -> null;
        };
        if (key == null) {
            return null;
        }

        boolean realStorage;
        if (key.equals("ENDER_CHEST")) {
            realStorage = inventory.equals(player.getEnderChest());
        } else {
            realStorage = holder instanceof Container
                    || holder instanceof DoubleChest
                    || (holder instanceof Vehicle && !(holder instanceof AbstractHorse)); // chest minecarts/boats
        }
        if (!realStorage) {
            return null;
        }

        return plugin.getConfigManager().getAllowedContainers().contains(key) ? key : null;
    }

    private boolean isTrappedChest(Inventory inventory) {
        Location location = inventory.getLocation();
        return location != null
                && location.isChunkLoaded()
                && location.getBlock().getType() == Material.TRAPPED_CHEST;
    }

    /** Spam protection. Returns false if the player sorted too recently. */
    public boolean tryAcquireCooldown(Player player) {
        int perSecond = plugin.getConfigManager().getMaxSortsPerSecond();
        if (perSecond <= 0) {
            return true;
        }
        long now = System.currentTimeMillis();
        long interval = 1000L / perSecond;
        Long last = lastSort.get(player.getUniqueId());
        if (last != null && now - last < interval) {
            return false;
        }
        lastSort.put(player.getUniqueId(), now);
        return true;
    }

    public void forget(Player player) {
        lastSort.remove(player.getUniqueId());
    }

    public void clearCooldowns() {
        lastSort.clear();
    }

    // ------------------------------------------------------------------
    // Sorting
    // ------------------------------------------------------------------

    /**
     * Sorts the hotbar (slots 0-8) or the main inventory (slots 9-35) of a player.
     * Armor, offhand and crafting slots are never touched.
     */
    public Result sortPlayerInventory(Player player, Inventory inventory, boolean isHotbar) {
        int start = isHotbar ? 0 : 9;
        int end = isHotbar ? 8 : 35;
        return sortRange(player, inventory, start, end);
    }

    /** Sorts every slot of a container. */
    public Result sortInventory(Player player, Inventory inventory) {
        return sortRange(player, inventory, 0, inventory.getSize() - 1);
    }

    private Result sortRange(Player player, Inventory inventory, int start, int end) {
        ItemStack[] original = new ItemStack[end - start + 1];
        try {
            List<ItemStack> items = new ArrayList<>();
            for (int i = start; i <= end; i++) {
                ItemStack item = inventory.getItem(i);
                original[i - start] = item == null ? null : item.clone();
                if (item != null && !item.isEmpty()) {
                    items.add(item.clone());
                }
            }

            if (items.isEmpty()) {
                return Result.NOTHING_TO_SORT;
            }

            if (plugin.getConfigManager().shouldStackItems()) {
                items = stackSimilarItems(items);
            }
            sortItems(items);

            for (int i = start; i <= end; i++) {
                inventory.setItem(i, null);
            }

            int slot = start;
            List<ItemStack> leftovers = new ArrayList<>();
            for (ItemStack item : items) {
                if (slot <= end) {
                    inventory.setItem(slot++, item);
                } else {
                    leftovers.add(item);
                }
            }

            // Should never happen, but never delete items: give back or drop at the player's feet.
            if (!leftovers.isEmpty()) {
                plugin.getLogger().warning("Sorting produced " + leftovers.size()
                        + " overflow stack(s) for " + player.getName() + "; returning them to the player.");
                for (ItemStack extra : player.getInventory().addItem(leftovers.toArray(new ItemStack[0])).values()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), extra);
                }
            }

            return Result.SORTED;

        } catch (Exception e) {
            plugin.getLogger().severe("Error while sorting for " + player.getName() + ": " + e.getMessage());
            if (plugin.getConfigManager().isVerbose()) {
                e.printStackTrace();
            }
            // Put everything back exactly as it was.
            for (int i = start; i <= end; i++) {
                inventory.setItem(i, original[i - start]);
            }
            return Result.FAILED;
        }
    }

    /**
     * Merges partial stacks of identical items (same type, name, enchants, components...).
     * Never splits a stack and never creates more stacks than it started with.
     */
    private List<ItemStack> stackSimilarItems(List<ItemStack> items) {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack item : items) {
            for (ItemStack target : result) {
                if (item.getAmount() <= 0) {
                    break;
                }
                int space = target.getMaxStackSize() - target.getAmount();
                if (space > 0 && target.isSimilar(item)) {
                    int moved = Math.min(space, item.getAmount());
                    target.setAmount(target.getAmount() + moved);
                    item.setAmount(item.getAmount() - moved);
                }
            }
            if (item.getAmount() > 0) {
                result.add(item);
            }
        }
        return result;
    }

    private void sortItems(List<ItemStack> items) {
        ItemCategorizer categorizer = plugin.getItemCategorizer();
        List<String> order = plugin.getConfigManager().getCategoryOrder();

        Comparator<ItemStack> comparator = Comparator.comparingInt(
                item -> categorizer.getCategoryPriority(categorizer.getCategory(item), order));

        if (plugin.getConfigManager().shouldSortAlphabetically()) {
            comparator = comparator
                    .thenComparing(item -> item.getType().name())
                    .thenComparing(Comparator.comparingInt(ItemStack::getAmount).reversed());
        }

        items.sort(comparator); // stable: equal items keep their current order
    }
}
