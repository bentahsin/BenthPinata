package com.bentahsin.benthPinata.commands.impl;

import com.bentahsin.benthPinata.configuration.MessageManager;
import com.bentahsin.benthPinata.stats.PlayerStatsService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class PinataStatsResetCommand {

    private final PlayerStatsService playerStatsService;
    private final MessageManager messageManager;

    public PinataStatsResetCommand(PlayerStatsService playerStatsService, MessageManager messageManager) {
        this.playerStatsService = playerStatsService;
        this.messageManager = messageManager;
    }

    @SuppressWarnings("deprecation") // Bukkit.getOfflinePlayer(String) için
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            messageManager.sendMessage(sender, "stats.reset.usage");
            return;
        }

        String target = args[1];

        if (target.equalsIgnoreCase("all")) {
            playerStatsService.resetAllStats();
            messageManager.sendMessage(sender, "stats.reset.all-success");
        } else {
            OfflinePlayer playerToReset = Bukkit.getOfflinePlayer(target);
            if (!playerToReset.hasPlayedBefore() && playerToReset.getName() == null) {
                messageManager.sendMessage(sender, "stats.reset.player-not-found", "%player%", target);
                return;
            }
            playerStatsService.resetPlayerStats(playerToReset.getUniqueId());
            messageManager.sendMessage(sender, "stats.reset.player-success", "%player%", playerToReset.getName());
        }
    }
}