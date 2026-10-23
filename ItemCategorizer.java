package com.Alakaii.smartsort.sorting;

import com.Alakaii.smartsort.SmartSortPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Puts every item into one of: weapons, tools, armor, food, blocks, items, misc.
 * Built by name patterns so new items (spears, maces, copper gear, etc.) are picked up
 * automatically on newer Minecraft versions.
 */
public class ItemCategorizer {

    private static final Set<String> WEAPON_NAMES = Set.of(
            "BOW", "CROSSBOW", "TRIDENT", "MACE", "ARROW", "SPECTRAL_ARROW", "TIPPED_ARROW",
            "WIND_CHARGE", "FIREWORK_ROCKET");

    private static final Set<String> TOOL_NAMES = Set.of(
            "SHEARS", "FISHING_ROD", "FLINT_AND_STEEL", "SHIELD", "BRUSH", "SPYGLASS",
            "CARROT_ON_A_STICK", "WARPED_FUNGUS_ON_A_STICK", "COMPASS", "RECOVERY_COMPASS",
            "CLOCK", "LEAD", "BUCKET");

    private static final Set<String> ARMOR_NAMES = Set.of("ELYTRA", "TURTLE_HELMET", "SADDLE");

    private final SmartSortPlugin plugin;
    private final Map<Material, String> categories = new EnumMap<>(Material.class);

    public ItemCategorizer(SmartSortPlugin plugin) {
        this.plugin = plugin;
        initializeCategories();
    }

    @SuppressWarnings("deprecation") // Material#isEdible/isBlock/isItem are fine for simple grouping
    private void initializeCategories() {
        for (Material mat : Material.values()) {
            String name = mat.name();
            if (name.startsWith("LEGACY_") || mat.isAir()) {
                continue;
            }

            String category;
            if (isWeapon(name)) {
                category = "weapons";
            } else if (isTool(name)) {
                category = "tools";
            } else if (isArmor(name)) {
                category = "armor";
            } else if (mat.isEdible()) {
                category = "food";
            } else if (mat.isBlock()) {
                category = "blocks";
            } else if (mat.isItem()) {
                category = "items";
            } else {
                category = "misc";
            }
            categories.put(mat, category);
        }
    }

    private boolean isWeapon(String name) {
        return name.endsWith("_SWORD") || name.endsWith("_SPEAR") || WEAPON_NAMES.contains(name);
    }

    private boolean isTool(String name) {
        return name.endsWith("_PICKAXE") || name.endsWith("_AXE") || name.endsWith("_SHOVEL")
                || name.endsWith("_HOE") || TOOL_NAMES.contains(name);
    }

    private boolean isArmor(String name) {
        return name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE") || name.endsWith("_LEGGINGS")
                || name.endsWith("_BOOTS") || name.endsWith("_ARMOR")
                || ARMOR_NAMES.contains(name);
    }

    public String getCategory(ItemStack item) {
        if (item == null || item.isEmpty()) {
            return "misc";
        }
        return categories.getOrDefault(item.getType(), "misc");
    }

    public int getCategoryPriority(String category, List<String> order) {
        int index = order.indexOf(category);
        return index == -1 ? order.size() : index;
    }

    public int getCategoryPriority(String category) {
        return getCategoryPriority(category, plugin.getConfigManager().getCategoryOrder());
    }
}
