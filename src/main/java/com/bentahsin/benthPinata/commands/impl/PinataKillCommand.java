package com.bentahsin.benthPinata.commands.impl;

import com.bentahsin.benthPinata.commands.ISubCommand;
import com.bentahsin.benthPinata.configuration.MessageManager;
import com.bentahsin.benthPinata.pinata.PinataRepository;
import com.bentahsin.benthPinata.pinata.PinataService;
import com.bentahsin.benthPinata.pinata.model.Pinata;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class PinataKillCommand implements ISubCommand {

    private final PinataService pinataService;
    private final PinataRepository pinataRepository;
    private final MessageManager messageManager;

    public PinataKillCommand(PinataService pinataService, PinataRepository pinataRepository, MessageManager messageManager) {
        this.pinataService = pinataService;
        this.pinataRepository = pinataRepository;
        this.messageManager = messageManager;
    }

    @Override
    public String getName() {
        return "kill";
    }

    @Override
    public String getPermission() {
        return "benthpinata.command.kill";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            messageManager.sendMessage(sender, "kill-command-usage");
            return;
        }

        int pinataId;
        try {
            pinataId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            messageManager.sendMessage(sender, "kill-command-invalid-id", "%id%", args[0]);
            return;
        }

        List<Pinata> activePinatas = new ArrayList<>(pinataRepository.findAll());

        // Kullanıcı 1'den başlar, liste 0'dan. ID'nin geçerli aralıkta olup olmadığını kontrol et.
        if (pinataId <= 0 || pinataId > activePinatas.size()) {
            messageManager.sendMessage(sender, "kill-command-invalid-id", "%id%", args[0]);
            return;
        }

        // Doğru Piñata'yı listeden al (index = id - 1)
        Pinata pinataToKill = activePinatas.get(pinataId - 1);

        pinataService.killPinata(pinataToKill.getUniqueId());
        messageManager.sendMessage(sender, "kill-command-success", "%id%", String.valueOf(pinataId), "%type%", pinataToKill.getType().id());
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            int size = pinataRepository.findAll().size();
            List<String> suggestions = new ArrayList<>();
            for (int i = 1; i <= size; i++) {
                suggestions.add(String.valueOf(i));
            }
            return suggestions;
        }
        return List.of();
    }
}