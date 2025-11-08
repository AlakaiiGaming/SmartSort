package com.Alakaii.smartsort;

import com.Alakaii.smartsort.commands.SmartSortCommand;
import com.Alakaii.smartsort.listeners.InventoryListener;
import com.Alakaii.smartsort.sorting.ItemCategorizer;
import com.Alakaii.smartsort.sorting.SortingManager;
import com.Alakaii.smartsort.utils.ConfigManager;
import com.Alakaii.smartsort.utils.MessageUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class SmartSortPlugin extends JavaPlugin {
    
    private static SmartSortPlugin instance;
    private ConfigManager configManager;
    private SortingManager sortingManager;
    private ItemCategorizer itemCategorizer;
    private MessageUtil messageUtil;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.messageUtil = new MessageUtil(this);
        this.itemCategorizer = new ItemCategorizer(this);
        this.sortingManager = new SortingManager(this);
        
        // Load configuration
        configManager.loadConfig();
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        
        // Register commands
        getCommand("smartsort").setExecutor(new SmartSortCommand(this));
        
        // Log startup
        getLogger().info("SmartSort v1.1.0 has been enabled!");
        getLogger().info("Now with granular permission support!");
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
}
