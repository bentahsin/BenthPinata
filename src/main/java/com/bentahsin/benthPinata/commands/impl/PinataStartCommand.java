package com.bentahsin.benthPinata.commands.impl;

import com.bentahsin.benthPinata.commands.ISubCommand;
import com.bentahsin.benthPinata.configuration.MessageManager;
import com.bentahsin.benthPinata.pinata.PinataService;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PinataStartCommand implements ISubCommand {

    private final PinataService pinataService;
    private final MessageManager messageManager;

    public PinataStartCommand(PinataService pinataService, MessageManager messageManager) {
        this.pinataService = pinataService;
        this.messageManager = messageManager;
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getPermission() {
        return "benthpinata.command.start";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            messageManager.sendMessage(sender, "start-command-usage");
            return;
        }

        String pinataTypeName = args[0];
        boolean success = pinataService.startEvent(pinataTypeName);

        if (!success) {
            messageManager.sendMessage(sender, "pinata-type-not-found");
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            String currentArg = args[0].toLowerCase();
            return pinataService.getLoadedTypeIds().stream()
                    .filter(type -> type.toLowerCase().startsWith(currentArg))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}