package com.Alakaii.smartsort.commands;

import com.Alakaii.smartsort.SmartSortPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SmartSortCommand implements CommandExecutor {
    
    private final SmartSortPlugin plugin;
    
    public SmartSortCommand(SmartSortPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "reload":
                if (!sender.hasPermission("smartsort.admin")) {
                    plugin.getMessageUtil().sendMessage(sender, "no-permission");
                    return true;
                }
                plugin.getConfigManager().loadConfig();
                plugin.getMessageUtil().sendMessage(sender, "plugin-reloaded");
                break;
                
            case "help":
                sendHelp(sender);
                break;
                
            default:
                sendHelp(sender);
                break;
        }
        
        return true;
    }
    
    private void sendHelp(CommandSender sender) {
        plugin.getMessageUtil().sendMessage(sender, "help-header");
        plugin.getMessageUtil().sendMessage(sender, "help-usage");
        plugin.getMessageUtil().sendMessage(sender, "help-permission");
        plugin.getMessageUtil().sendMessage(sender, "help-permissions-list");
        plugin.getMessageUtil().sendMessage(sender, "help-commands");
    }
}
