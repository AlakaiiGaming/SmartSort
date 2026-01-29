package com.Alakaii.smartsort;

import com.Alakaii.smartsort.commands.SmartSortCommand;
import com.Alakaii.smartsort.listeners.InventoryListener;
import com.Alakaii.smartsort.listeners.PlayerJoinListener;
import com.Alakaii.smartsort.sorting.ItemCategorizer;
import com.Alakaii.smartsort.sorting.SortingManager;
import com.Alakaii.smartsort.utils.ConfigManager;
import com.Alakaii.smartsort.utils.MessageUtil;
import com.Alakaii.smartsort.utils.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;

public class SmartSortPlugin extends JavaPlugin {
    
    private static SmartSortPlugin instance;
    private ConfigManager configManager;
    private SortingManager sortingManager;
    private ItemCategorizer itemCategorizer;
    private MessageUtil messageUtil;
    private UpdateChecker updateChecker;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.messageUtil = new MessageUtil(this);
        this.itemCategorizer = new ItemCategorizer(this);
        this.sortingManager = new SortingManager(this);
        this.updateChecker = new UpdateChecker(this);
        
        // Load configuration (automatically updates old configs)
        configManager.loadConfig();
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        
        // Register commands
        getCommand("smartsort").setExecutor(new SmartSortCommand(this));
        
        // Check for updates
        updateChecker.checkForUpdates();
        
        // Log startup
        getLogger().info("SmartSort v1.2.1 has been enabled!");
        getLogger().info("Features: Granular permissions + Auto-config updates + Update notifications");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("SmartSort has been disabled!");
    }
    
    public static SmartSortPlugin getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public SortingManager getSortingManager() {
        return sortingManager;
    }
    
    public ItemCategorizer getItemCategorizer() {
        return itemCategorizer;
    }
    
    public MessageUtil getMessageUtil() {
        return messageUtil;
    }
    
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
}
