package com.Alakaii.smartsort;

import com.Alakaii.smartsort.commands.SmartSortCommand;
import com.Alakaii.smartsort.listeners.InventoryListener;
import com.Alakaii.smartsort.sorting.SortingManager;
import com.Alakaii.smartsort.utils.ConfigManager;
import com.Alakaii.smartsort.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SmartSortPlugin extends JavaPlugin {

    private static SmartSortPlugin instance;
    private ConfigManager configManager;
    private SortingManager sortingManager;
    private MessageUtil messageUtil;

    @Override
    public void onEnable() {
        instance = this;

        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.messageUtil = new MessageUtil(this);
        this.sortingManager = new SortingManager(this);

        // Load configuration
        configManager.loadConfig();

        // Register events
        registerEvents();

        // Register commands
        registerCommands();

        // Log successful startup
        getLogger().info("SmartSort v" + getDescription().getVersion() + " has been enabled!");
        getLogger().info("Created by Alakaii");

        if (configManager.isVerbose()) {
            getLogger().info("Verbose logging is enabled");
            getLogger().info("Permission required: smartsort.use");
            getLogger().info("Hotkey: Shift+Right-Click on empty slots");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("SmartSort has been disabled!");
        instance = null;
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), this);
    }

    private void registerCommands() {
        SmartSortCommand commandExecutor = new SmartSortCommand(this);
        if (getCommand("smartsort") != null) {
            getCommand("smartsort").setExecutor(commandExecutor);
            getCommand("smartsort").setTabCompleter(commandExecutor);
        }
    }

    public void reload() {
        configManager.reloadConfig();
        getLogger().info("SmartSort configuration reloaded!");
    }

    // Static getters
    public static SmartSortPlugin getInstance() {
        return instance;
    }

    // Instance getters
    public ConfigManager getConfigManager() {
        return configManager;
    }

    public SortingManager getSortingManager() {
        return sortingManager;
    }

    public MessageUtil getMessageUtil() {
        return messageUtil;
    }
}