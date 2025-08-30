package com.bentahsin.benthPinata.commands.impl;

import com.bentahsin.benthPinata.commands.ISubCommand;
import com.bentahsin.benthPinata.configuration.MessageManager;
import com.bentahsin.benthPinata.pinata.PinataRepository;
import com.bentahsin.benthPinata.pinata.model.Pinata;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PinataListCommand implements ISubCommand {

    private final PinataRepository pinataRepository;
    private final MessageManager messageManager;

    public PinataListCommand(PinataRepository pinataRepository, MessageManager messageManager) {
        this.pinataRepository = pinataRepository;
        this.messageManager = messageManager;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getPermission() {
        return "benthpinata.command.list";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        List<Pinata> activePinatas = new ArrayList<>(pinataRepository.findAll());

        if (activePinatas.isEmpty()) {
            messageManager.sendMessage(sender, "list-command-empty");
            return;
        }

        messageManager.sendMessageList(sender, "list-command-header");
        for (int i = 0; i < activePinatas.size(); i++) {
            Pinata pinata = activePinatas.get(i);
            sender.sendMessage(messageManager.getMessage("list-command-format",
                    "%id%", String.valueOf(i + 1),
                    "%type%", pinata.getType().getId(),
                    "%health%", String.valueOf(pinata.getCurrentHealth()),
                    "%max_health%", String.valueOf(pinata.getType().getMaxHealth()),
                    "%world%", pinata.getEntity().getWorld().getName(),
                    "%x%", String.valueOf(pinata.getEntity().getLocation().getBlockX()),
                    "%y%", String.valueOf(pinata.getEntity().getLocation().getBlockY()),
                    "%z%", String.valueOf(pinata.getEntity().getLocation().getBlockZ())
            ));
        }
        messageManager.sendMessageList(sender, "list-command-footer");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}