package com.Alakaii.smartsort.utils;

import com.Alakaii.smartsort.SmartSortPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ConfigManager {

    private final SmartSortPlugin plugin;
    private FileConfiguration config;
    private Set<String> allowedContainers = Set.of();

    public ConfigManager(SmartSortPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();

        updateConfig();

        Set<String> containers = new HashSet<>();
        for (String name : config.getStringList("allowed-containers")) {
            containers.add(name.trim().toUpperCase(Locale.ROOT));
        }
        this.allowedContainers = Set.copyOf(containers);
    }

    /**
     * Adds keys that exist in the bundled config but are missing from the server's config,
     * keeping every value the server owner already set.
     */
    private void updateConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        FileConfiguration currentConfig = YamlConfiguration.loadConfiguration(configFile);

        InputStream resource = plugin.getResource("config.yml");
        if (resource == null) {
            return;
        }

        FileConfiguration defaultConfig;
        try (Reader reader = new InputStreamReader(resource, StandardCharsets.UTF_8)) {
            defaultConfig = YamlConfiguration.loadConfiguration(reader);
        } catch (Exception e) {
            plugin.getLogger().warning("Could not read the default config: " + e.getMessage());
            return;
        }

        boolean updated = false;
        for (String key : defaultConfig.getKeys(true)) {
            if (!currentConfig.contains(key)) {
                currentConfig.set(key, defaultConfig.get(key));
                updated = true;
                plugin.getLogger().info("Added missing config key: " + key);
            }
        }

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
        return config.getStringList("disabled-worlds").contains(worldName);
    }

    public boolean isVerbose() {
        return config.getBoolean("verbose", false);
    }

    public boolean usePermissions() {
        return config.getBoolean("use-permissions", true);
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

    /** Upper-cased container names from allowed-containers. */
    public Set<String> getAllowedContainers() {
        return allowedContainers;
    }

    public int getMaxSortsPerSecond() {
        return config.getInt("max-sorting-operations-per-second", 10);
    }
}
