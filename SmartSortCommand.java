package com.Alakaii.smartsort.commands;

import com.Alakaii.smartsort.SmartSortPlugin;
import com.Alakaii.smartsort.sorting.SortingManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SmartSortCommand implements TabExecutor {

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

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "reload" -> {
                if (!sender.hasPermission("smartsort.admin")) {
                    plugin.getMessageUtil().sendMessage(sender, "no-permission");
                    return true;
                }
                plugin.getConfigManager().loadConfig();
                plugin.getMessageUtil().sendMessage(sender, "plugin-reloaded");
            }
            case "inv", "inventory" -> sortOwnInventory(sender, false);
            case "hotbar" -> sortOwnInventory(sender, true);
            default -> sendHelp(sender);
        }
        return true;
    }

    private void sortOwnInventory(CommandSender sender, boolean hotbar) {
        if (!(sender instanceof Player player)) {
            plugin.getMessageUtil().sendMessage(sender, "players-only");
            return;
        }

        SortingManager sorter = plugin.getSortingManager();
        if (!sorter.canSortPlayerInventory(player)) {
            plugin.getMessageUtil().sendMessage(player, "no-permission-inventory");
            return;
        }
        if (plugin.getConfigManager().isWorldDisabled(player.getWorld().getName())) {
            plugin.getMessageUtil().sendMessage(player, "sorting-disabled-world");
            return;
        }
        if (!sorter.tryAcquireCooldown(player)) {
            return;
        }

        switch (sorter.sortPlayerInventory(player, player.getInventory(), hotbar)) {
            case SORTED -> plugin.getMessageUtil().sendMessage(player, "sorted-successfully");
            case NOTHING_TO_SORT -> plugin.getMessageUtil().sendMessage(player, "nothing-to-sort");
            case FAILED -> plugin.getMessageUtil().sendMessage(player, "sorting-failed");
        }
    }

    private void sendHelp(CommandSender sender) {
        plugin.getMessageUtil().sendMessage(sender, "help-header");
        plugin.getMessageUtil().sendMessage(sender, "help-usage");
        plugin.getMessageUtil().sendMessage(sender, "help-commands");
        plugin.getMessageUtil().sendMessage(sender, "help-permission");
        plugin.getMessageUtil().sendMessage(sender, "help-permissions-list");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> matches = new ArrayList<>();
        if (args.length != 1) {
            return matches;
        }
        List<String> options = new ArrayList<>(List.of("inv", "hotbar", "help"));
        if (sender.hasPermission("smartsort.admin")) {
            options.add("reload");
        }
        String typed = args[0].toLowerCase(Locale.ROOT);
        for (String option : options) {
            if (option.startsWith(typed)) {
                matches.add(option);
            }
        }
        return matches;
    }
}
