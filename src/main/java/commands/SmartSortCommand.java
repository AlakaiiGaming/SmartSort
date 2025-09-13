package com.Alakaii.smartsort.commands;

import com.Alakaii.smartsort.SmartSortPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SmartSortCommand implements CommandExecutor, TabCompleter {

    private final SmartSortPlugin plugin;

    public SmartSortCommand(SmartSortPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender has permission for basic usage
        if (!sender.hasPermission("smartsort.use")) {
            plugin.getMessageUtil().sendMessage(sender, "no-permission");
            return true;
        }

        // No arguments - show help
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "help":
                showHelp(sender);
                break;

            case "on":
            case "enable":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    plugin.getConfigManager().setPlayerEnabled(player, true);
                    sender.sendMessage("§aSmartSort enabled! You can now sort inventories with Shift+Right-Click.");
                } else {
                    sender.sendMessage("§cThis command can only be used by players.");
                }
                break;

            case "off":
            case "disable":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    plugin.getConfigManager().setPlayerEnabled(player, false);
                    sender.sendMessage("§cSmartSort disabled. You can no longer sort inventories.");
                } else {
                    sender.sendMessage("§cThis command can only be used by players.");
                }
                break;

            case "toggle":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    boolean isEnabled = plugin.getConfigManager().isPlayerEnabled(player);
                    plugin.getConfigManager().setPlayerEnabled(player, !isEnabled);
                    if (!isEnabled) {
                        sender.sendMessage("§aSmartSort enabled! You can now sort inventories with Shift+Right-Click.");
                    } else {
                        sender.sendMessage("§cSmartSort disabled. You can no longer sort inventories.");
                    }
                } else {
                    sender.sendMessage("§cThis command can only be used by players.");
                }
                break;

            case "reload":
                if (!sender.hasPermission("smartsort.admin")) {
                    plugin.getMessageUtil().sendMessage(sender, "no-permission");
                    return true;
                }

                plugin.reload();
                plugin.getMessageUtil().sendMessage(sender, "plugin-reloaded");
                break;

            case "version":
            case "info":
                showInfo(sender);
                break;

            default:
                showHelp(sender);
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("help", "on", "off", "toggle", "version", "info");

            if (sender.hasPermission("smartsort.admin")) {
                subCommands = new ArrayList<>(subCommands);
                subCommands.add("reload");
            }

            String partial = args[0].toLowerCase();
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(partial)) {
                    completions.add(subCommand);
                }
            }
        }

        return completions;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6=== SmartSort Help ===");
        sender.sendMessage("§eTo sort: §fShift+Right-Click on an empty slot");
        sender.sendMessage("§eCommands:");
        sender.sendMessage("  §f/smartsort on §7- Enable sorting");
        sender.sendMessage("  §f/smartsort off §7- Disable sorting");
        sender.sendMessage("  §f/smartsort toggle §7- Toggle sorting on/off");
        sender.sendMessage("  §f/smartsort help §7- Show this help");
        sender.sendMessage("  §f/smartsort info §7- Plugin information");

        if (sender.hasPermission("smartsort.admin")) {
            sender.sendMessage("§eAdmin: §f/smartsort reload §7- Reload configuration");
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            boolean isEnabled = plugin.getConfigManager().isPlayerEnabled(player);
            sender.sendMessage("§eStatus: §f" + (isEnabled ? "§aEnabled" : "§cDisabled"));
        }
    }

    private void showInfo(CommandSender sender) {
        sender.sendMessage("§6=== SmartSort Information ===");
        sender.sendMessage("§eVersion: §f" + plugin.getDescription().getVersion());
        sender.sendMessage("§eAuthor: §fAlakaii");
        sender.sendMessage("§eWebsite: §f" + plugin.getDescription().getWebsite());
        sender.sendMessage("§eDescription: §f" + plugin.getDescription().getDescription());

        if (sender instanceof Player) {
            Player player = (Player) sender;
            boolean isEnabled = plugin.getConfigManager().isPlayerEnabled(player);
            sender.sendMessage("§eYour Status: §f" + (isEnabled ? "§aEnabled" : "§cDisabled"));
        }
    }
}