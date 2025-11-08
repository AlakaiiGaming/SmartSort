package com.Alakaii.smartsort.utils;

import com.Alakaii.smartsort.SmartSortPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {
    
    private final SmartSortPlugin plugin;
    private FileConfiguration config;
    
    public ConfigManager(SmartSortPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }
    
    public boolean isWorldDisabled(String worldName) {
        List<String> disabledWorlds = config.getStringList("disabled-worlds");
        return disabledWorlds.contains(worldName);
    }
    
    public boolean isVerbose() {
        return config.getBoolean("verbose", false);
    }
    
    public boolean usePermissions() {
        return config.getBoolean("use-permissions", true);
    }
    
    public boolean isProtectionIntegrationEnabled() {
        return config.getBoolean("protection-integration.enabled", true);
    }
    
    public boolean shouldCheckContainerAccess() {
        return config.getBoolean("protection-integration.check-container-access", true);
    }
    
    public boolean shouldStackItems() {
        return config.getBoolean("sorting.stack-items", true);
    }
    
    public boolean shouldSortAlphabetically() {
        return config.getBoolean("sorting.alphabetical", true);
    }
    
    public List<String> getCategoryOrder() {
        return config.getStringList("sorting.category-order");
    }
    
    public List<String> getAllowedContainers() {
        return config.getStringList("allowed-containers");
    }
}
