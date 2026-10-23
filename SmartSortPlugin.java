package com.Alakaii.smartsort;

import com.Alakaii.smartsort.commands.SmartSortCommand;
import com.Alakaii.smartsort.listeners.InventoryListener;
import com.Alakaii.smartsort.listeners.PlayerJoinListener;
import com.Alakaii.smartsort.sorting.ItemCategorizer;
import com.Alakaii.smartsort.sorting.SortingManager;
import com.Alakaii.smartsort.utils.ConfigManager;
import com.Alakaii.smartsort.utils.MessageUtil;
import com.Alakaii.smartsort.utils.UpdateChecker;
import org.bukkit.command.PluginCommand;
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

        this.configManager = new ConfigManager(this);
        this.messageUtil = new MessageUtil(this);
        this.itemCategorizer = new ItemCategorizer(this);
        this.sortingManager = new SortingManager(this);
        this.updateChecker = new UpdateChecker(this);

        // Load configuration (automatically adds new keys to old configs)
        configManager.loadConfig();

        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        PluginCommand command = getCommand("smartsort");
        if (command != null) {
            SmartSortCommand executor = new SmartSortCommand(this);
            command.setExecutor(executor);
            command.setTabCompleter(executor);
        }

        updateChecker.checkForUpdates();

        getLogger().info("SmartSort v" + getPluginMeta().getVersion() + " has been enabled!");
    }

    @Override
    public void onDisable() {
        if (sortingManager != null) {
            sortingManager.clearCooldowns();
        }
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
