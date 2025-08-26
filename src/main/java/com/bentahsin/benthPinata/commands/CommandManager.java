// src/main/java/com/bentahsin/benthPinata/commands/CommandManager.java

package com.bentahsin.benthPinata.commands;

import com.bentahsin.benthPinata.configuration.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Ana /pinata komutunu ve tab tamamlamasını yönetir.
 * Gelen argümanlara göre ilgili alt komutu (ISubCommand) çalıştırır veya tab tamamlama için ona delege eder.
 */
public class CommandManager implements CommandExecutor, TabCompleter {

    private final MessageManager messageManager;
    private final Map<String, ISubCommand> subCommands = new HashMap<>();

    public CommandManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    public void registerCommand(ISubCommand subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            ISubCommand helpCommand = subCommands.get("help");
            if (helpCommand != null) {
                if (helpCommand.getPermission() != null && !sender.hasPermission(helpCommand.getPermission())) {
                    messageManager.sendMessage(sender, "no-permission");
                } else {
                    helpCommand.execute(sender, new String[0]);
                }
            } else {
                messageManager.sendMessage(sender, "invalid-command-usage");
            }
            return true;
        }

        String subCommandName = args[0].toLowerCase();
        ISubCommand subCommand = subCommands.get(subCommandName);

        if (subCommand == null) {
            messageManager.sendMessage(sender, "invalid-command-usage");
            return true;
        }

        if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) {
            messageManager.sendMessage(sender, "no-permission");
            return true;
        }

        String[] subCommandArgs = Arrays.copyOfRange(args, 1, args.length);
        subCommand.execute(sender, subCommandArgs);
        return true;
    }

    /**
     * Tab tamamlamayıcı
     */
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            String currentArg = args[0].toLowerCase();
            return subCommands.values().stream()
                    .filter(sub -> sub.getPermission() == null || sender.hasPermission(sub.getPermission()))
                    .map(ISubCommand::getName)
                    .filter(name -> name.toLowerCase().startsWith(currentArg))
                    .collect(Collectors.toList());
        }

        if (args.length > 1) {
            ISubCommand subCommand = subCommands.get(args[0].toLowerCase());
            if (subCommand != null && (subCommand.getPermission() == null || sender.hasPermission(subCommand.getPermission()))) {
                String[] subCommandArgs = Arrays.copyOfRange(args, 1, args.length);
                return subCommand.tabComplete(sender, subCommandArgs);
            }
        }

        return Collections.emptyList();
    }
}