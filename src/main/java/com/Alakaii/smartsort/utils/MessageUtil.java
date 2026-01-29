package com.Alakaii.smartsort.utils;

import com.Alakaii.smartsort.SmartSortPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageUtil {
    
    private final SmartSortPlugin plugin;
    
    public MessageUtil(SmartSortPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void sendMessage(CommandSender sender, String key) {
        String message = plugin.getConfig().getString("messages." + key, "&cMessage not found: " + key);
        sender.sendMessage(colorize(message));
    }
    
    public String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
