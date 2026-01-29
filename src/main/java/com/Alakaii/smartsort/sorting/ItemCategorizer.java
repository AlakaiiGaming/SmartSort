package com.Alakaii.smartsort.sorting;

import com.Alakaii.smartsort.SmartSortPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemCategorizer {
    
    private final SmartSortPlugin plugin;
    private final Map<String, Set<Material>> categories;
    
    public ItemCategorizer(SmartSortPlugin plugin) {
        this.plugin = plugin;
        this.categories = new HashMap<>();
        initializeCategories();
    }
    
    private void initializeCategories() {
        // Weapons
        Set<Material> weapons = new HashSet<>();
        weapons.add(Material.WOODEN_SWORD);
        weapons.add(Material.STONE_SWORD);
        weapons.add(Material.IRON_SWORD);
        weapons.add(Material.GOLDEN_SWORD);
        weapons.add(Material.DIAMOND_SWORD);
        weapons.add(Material.NETHERITE_SWORD);
        weapons.add(Material.BOW);
        weapons.add(Material.CROSSBOW);
        weapons.add(Material.TRIDENT);
        weapons.add(Material.ARROW);
        weapons.add(Material.SPECTRAL_ARROW);
        weapons.add(Material.TIPPED_ARROW);
        categories.put("weapons", weapons);
        
        // Tools
        Set<Material> tools = new HashSet<>();
        for (Material mat : Material.values()) {
            String name = mat.name();
            if ((name.contains("PICKAXE") || name.contains("AXE") || name.contains("SHOVEL") || 
                 name.contains("HOE") || name.equals("SHEARS") || name.equals("FISHING_ROD") ||
                 name.equals("FLINT_AND_STEEL") || name.equals("SHIELD")) && !name.contains("SWORD")) {
                tools.add(mat);
            }
        }
        categories.put("tools", tools);
        
        // Armor
        Set<Material> armor = new HashSet<>();
        for (Material mat : Material.values()) {
            String name = mat.name();
            if (name.contains("HELMET") || name.contains("CHESTPLATE") || 
                name.contains("LEGGINGS") || name.contains("BOOTS") ||
                name.equals("ELYTRA") || name.equals("TURTLE_HELMET")) {
                armor.add(mat);
            }
        }
        categories.put("armor", armor);
        
        // Food
        Set<Material> food = new HashSet<>();
        for (Material mat : Material.values()) {
            if (mat.isEdible()) {
                food.add(mat);
            }
        }
        categories.put("food", food);
        
        // Blocks
        Set<Material> blocks = new HashSet<>();
        for (Material mat : Material.values()) {
            if (mat.isBlock() && !weapons.contains(mat) && !tools.contains(mat) && 
                !armor.contains(mat) && !food.contains(mat)) {
                blocks.add(mat);
            }
        }
        categories.put("blocks", blocks);
        
        // Items (non-block items that don't fit other categories)
        Set<Material> items = new HashSet<>();
        for (Material mat : Material.values()) {
            if (mat.isItem() && !mat.isBlock() && !weapons.contains(mat) && 
                !tools.contains(mat) && !armor.contains(mat) && !food.contains(mat)) {
                items.add(mat);
            }
        }
        categories.put("items", items);
        
        // Misc (everything else)
        categories.put("misc", new HashSet<>());
    }
    
    public String getCategory(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return "misc";
        }
        
        Material type = item.getType();
        
        for (Map.Entry<String, Set<Material>> entry : categories.entrySet()) {
            if (entry.getValue().contains(type)) {
                return entry.getKey();
            }
        }
        
        return "misc";
    }
    
    public int getCategoryPriority(String category) {
        List<String> order = plugin.getConfigManager().getCategoryOrder();
        int index = order.indexOf(category);
        return index == -1 ? order.size() : index;
    }
}
