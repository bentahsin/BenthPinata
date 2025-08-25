package com.bentahsin.benthPinata.commands.impl;

import com.bentahsin.benthPinata.commands.ISubCommand;
import com.bentahsin.benthPinata.configuration.MessageManager;
import org.bukkit.command.CommandSender;

/**
 * Oyunculara yardım menüsünü gösteren alt komut.
 */
public class PinataHelpCommand implements ISubCommand {

    private final MessageManager messageManager;

    public PinataHelpCommand(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getPermission() {
        return "benthpinata.command.help";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        messageManager.sendMessageList(sender, "help-command");
    }
}