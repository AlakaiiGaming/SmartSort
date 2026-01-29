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

            case "inv":
            case "inventory":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§cThis command can only be used by players!");
                    return true;
                }
                Player player = (Player) sender;

                // Check permission
                if (!player.hasPermission("smartsort.use") && !player.hasPermission("smartsort.inventory.sort")) {
                    plugin.getMessageUtil().sendMessage(player, "no-permission-inventory");
                    return true;
                }

                // Check if world is disabled
                if (plugin.getConfigManager().isWorldDisabled(player.getWorld().getName())) {
                    plugin.getMessageUtil().sendMessage(player, "sorting-disabled-world");
                    return true;
                }

                // Sort main inventory (slots 9-35)
                boolean success = plugin.getSortingManager().sortPlayerInventory(player, player.getInventory(), false);

                if (success) {
                    plugin.getMessageUtil().sendMessage(player, "sorted-successfully");
                } else {
                    plugin.getMessageUtil().sendMessage(player, "sorting-failed");
                }
                break;

            case "hotbar":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§cThis command can only be used by players!");
                    return true;
                }
                Player hotbarPlayer = (Player) sender;

                // Check permission
                if (!hotbarPlayer.hasPermission("smartsort.use") && !hotbarPlayer.hasPermission("smartsort.inventory.sort")) {
                    plugin.getMessageUtil().sendMessage(hotbarPlayer, "no-permission-inventory");
                    return true;
                }

                // Check if world is disabled
                if (plugin.getConfigManager().isWorldDisabled(hotbarPlayer.getWorld().getName())) {
                    plugin.getMessageUtil().sendMessage(hotbarPlayer, "sorting-disabled-world");
                    return true;
                }

                // Sort hotbar (slots 0-8)
                boolean hotbarSuccess = plugin.getSortingManager().sortPlayerInventory(hotbarPlayer, hotbarPlayer.getInventory(), true);

                if (hotbarSuccess) {
                    plugin.getMessageUtil().sendMessage(hotbarPlayer, "sorted-successfully");
                } else {
                    plugin.getMessageUtil().sendMessage(hotbarPlayer, "sorting-failed");
                }
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
        plugin.getMessageUtil().sendMessage(sender, "help-commands");
        plugin.getMessageUtil().sendMessage(sender, "help-permission");
        plugin.getMessageUtil().sendMessage(sender, "help-permissions-list");
    }
}