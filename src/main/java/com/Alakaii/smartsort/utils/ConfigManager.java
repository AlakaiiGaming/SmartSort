package com.Alakaii.smartsort.utils;

import com.Alakaii.smartsort.SmartSortPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

public class ConfigManager {
    
    private final SmartSortPlugin plugin;
    private FileConfiguration config;
    private String configVersion;
    
    public ConfigManager(SmartSortPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void loadConfig() {
        // Create config if it doesn't exist
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        
        // Update config if needed
        updateConfig();
    }
    
    /**
     * Automatically update config with new keys while preserving user settings
     */
    private void updateConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        
        // Load current config from file
        FileConfiguration currentConfig = YamlConfiguration.loadConfiguration(configFile);
        
        // Load default config from JAR
        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
            new InputStreamReader(plugin.getResource("config.yml"))
        );
        
        boolean updated = false;
        
        // Check for missing keys and add them
        for (String key : defaultConfig.getKeys(true)) {
            if (!currentConfig.contains(key)) {
                currentConfig.set(key, defaultConfig.get(key));
                updated = true;
                plugin.getLogger().info("Added missing config key: " + key);
            }
        }
        
        // Save if updated
        if (updated) {
            try {
                currentConfig.save(configFile);
                plugin.getLogger().info("Config has been updated with new options!");
                plugin.reloadConfig();
                this.config = plugin.getConfig();
            } catch (Exception e) {
                plugin.getLogger().warning("Could not save updated config: " + e.getMessage());
            }
        }
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
