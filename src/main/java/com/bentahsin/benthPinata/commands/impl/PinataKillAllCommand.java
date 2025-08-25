package com.bentahsin.benthPinata.commands.impl;

import com.bentahsin.benthPinata.commands.ISubCommand;
import com.bentahsin.benthPinata.configuration.MessageManager;
import com.bentahsin.benthPinata.pinata.PinataService;
import org.bukkit.command.CommandSender;

/**
 * Aktif olan tüm Piñata'ları ve ilişkili görevleri sonlandıran alt komut.
 */
public class PinataKillAllCommand implements ISubCommand {

    private final PinataService pinataService;
    private final MessageManager messageManager;

    public PinataKillAllCommand(PinataService pinataService, MessageManager messageManager) {
        this.pinataService = pinataService;
        this.messageManager = messageManager;
    }

    @Override
    public String getName() {
        return "killall";
    }

    @Override
    public String getPermission() {
        return "benthpinata.command.killall";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        pinataService.killAll();
        messageManager.sendMessage(sender, "killall-success");
    }
}