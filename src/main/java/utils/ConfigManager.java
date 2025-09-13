package com.Alakaii.smartsort.utils;

import com.Alakaii.smartsort.SmartSortPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ConfigManager {

    private final SmartSortPlugin plugin;
    private FileConfiguration config;
    private final Map<UUID, Boolean> playerEnabled = new HashMap<>();

    public ConfigManager(SmartSortPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        playerEnabled.clear(); // Clear cache on reload
    }

    public boolean isVerbose() {
        return config.getBoolean("verbose", false);
    }

    public boolean usePermissions() {
        return config.getBoolean("use-permissions", true);
    }

    public boolean isWorldDisabled(String worldName) {
        List<String> disabledWorlds = config.getStringList("disabled-worlds");
        return disabledWorlds.contains(worldName);
    }

    public List<String> getAllowedContainers() {
        return config.getStringList("allowed-containers");
    }

    public int getMaxSortingOperationsPerSecond() {
        return config.getInt("max-sorting-operations-per-second", 10);
    }

    public boolean shouldStackItems() {
        return config.getBoolean("sorting.stack-items", true);
    }

    public boolean isAlphabeticalSorting() {
        return config.getBoolean("sorting.alphabetical", true);
    }

    public List<String> getCategoryOrder() {
        return config.getStringList("sorting.category-order");
    }

    public boolean isProtectionIntegrationEnabled() {
        return config.getBoolean("protection-integration.enabled", true);
    }

    public boolean shouldCheckContainerAccess() {
        return config.getBoolean("protection-integration.check-container-access", true);
    }

    public String getMessage(String key) {
        return config.getString("messages." + key, "§cMessage not found: " + key);
    }

    // Player enable/disable methods
    public boolean isPlayerEnabled(Player player) {
        UUID uuid = player.getUniqueId();
        return playerEnabled.getOrDefault(uuid, true); // Default to enabled
    }

    public void setPlayerEnabled(Player player, boolean enabled) {
        UUID uuid = player.getUniqueId();
        playerEnabled.put(uuid, enabled);
    }

    public FileConfiguration getConfig() {
        return config;
    }
}