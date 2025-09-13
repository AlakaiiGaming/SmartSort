package com.Alakaii.smartsort.utils;

import com.Alakaii.smartsort.SmartSortPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageUtil {

    private final SmartSortPlugin plugin;

    public MessageUtil(SmartSortPlugin plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(CommandSender sender, String messageKey) {
        String message = plugin.getConfigManager().getMessage(messageKey);
        sendFormattedMessage(sender, message);
    }

    public void sendFormattedMessage(CommandSender sender, String message) {
        if (message == null || message.isEmpty()) {
            return;
        }

        String formattedMessage = colorize(message);
        sender.sendMessage(formattedMessage);
    }

    public String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String stripColor(String message) {
        return ChatColor.stripColor(message);
    }
}