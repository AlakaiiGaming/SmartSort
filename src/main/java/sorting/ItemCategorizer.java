package com.Alakaii.smartsort.sorting;

import org.bukkit.Material;

import java.util.*;

public class ItemCategorizer {

    private final Map<String, Integer> categoryPriorities;
    private final Map<Material, String> itemCategories;

    public ItemCategorizer() {
        this.categoryPriorities = new HashMap<>();
        this.itemCategories = new HashMap<>();

        initializeCategoryPriorities();
        initializeItemCategories();
    }

    private void initializeCategoryPriorities() {
        // Default category order - can be overridden by config
        categoryPriorities.put("weapons", 0);
        categoryPriorities.put("tools", 1);
        categoryPriorities.put("armor", 2);
        categoryPriorities.put("food", 3);
        categoryPriorities.put("blocks", 4);
        categoryPriorities.put("items", 5);
        categoryPriorities.put("misc", 6);
    }

    private void initializeItemCategories() {
        // Weapons
        addToCategory("weapons",
                Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
                Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD,
                Material.BOW, Material.CROSSBOW, Material.TRIDENT);

        // Tools
        addToCategory("tools",
                Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
                Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE,
                Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE,
                Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE,
                Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL,
                Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL,
                Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE,
                Material.GOLDEN_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE,
                Material.FISHING_ROD, Material.FLINT_AND_STEEL, Material.SHEARS);

        // Armor
        addToCategory("armor",
                Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
                Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
                Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
                Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
                Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
                Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS,
                Material.SHIELD);

        // Food
        addToCategory("food",
                Material.APPLE, Material.GOLDEN_APPLE, Material.ENCHANTED_GOLDEN_APPLE,
                Material.BREAD, Material.COOKED_BEEF, Material.COOKED_CHICKEN, Material.COOKED_COD,
                Material.COOKED_MUTTON, Material.COOKED_PORKCHOP, Material.COOKED_RABBIT,
                Material.COOKED_SALMON, Material.COOKIE, Material.CAKE, Material.PUMPKIN_PIE,
                Material.BEETROOT, Material.BEETROOT_SOUP, Material.CARROT, Material.GOLDEN_CARROT,
                Material.POTATO, Material.BAKED_POTATO, Material.POISONOUS_POTATO,
                Material.MUSHROOM_STEW, Material.RABBIT_STEW, Material.SUSPICIOUS_STEW);

        // Common blocks
        addToCategory("blocks",
                Material.DIRT, Material.GRASS_BLOCK, Material.STONE, Material.COBBLESTONE,
                Material.OAK_LOG, Material.BIRCH_LOG, Material.SPRUCE_LOG, Material.JUNGLE_LOG,
                Material.ACACIA_LOG, Material.DARK_OAK_LOG, Material.CRIMSON_STEM, Material.WARPED_STEM,
                Material.OAK_PLANKS, Material.BIRCH_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS,
                Material.ACACIA_PLANKS, Material.DARK_OAK_PLANKS, Material.CRIMSON_PLANKS, Material.WARPED_PLANKS,
                Material.SAND, Material.GRAVEL, Material.CLAY, Material.TERRACOTTA);

        // Common items/materials
        addToCategory("items",
                Material.COAL, Material.CHARCOAL, Material.IRON_INGOT, Material.GOLD_INGOT,
                Material.DIAMOND, Material.EMERALD, Material.NETHERITE_INGOT,
                Material.REDSTONE, Material.LAPIS_LAZULI, Material.QUARTZ,
                Material.STICK, Material.STRING, Material.LEATHER, Material.FEATHER,
                Material.BONE, Material.GUNPOWDER, Material.BLAZE_POWDER, Material.ENDER_PEARL);
    }

    private void addToCategory(String category, Material... materials) {
        for (Material material : materials) {
            itemCategories.put(material, category);
        }
    }

    public String getCategory(Material material) {
        return itemCategories.getOrDefault(material, "misc");
    }

    public int getCategoryPriority(String category) {
        return categoryPriorities.getOrDefault(category, 999);
    }

    public void updateCategoryPriorities(List<String> categoryOrder) {
        categoryPriorities.clear();
        for (int i = 0; i < categoryOrder.size(); i++) {
            categoryPriorities.put(categoryOrder.get(i), i);
        }
    }

    public Set<String> getAllCategories() {
        return new HashSet<>(categoryPriorities.keySet());
    }
}