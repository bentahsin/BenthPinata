package com.bentahsin.benthPinata.commands.impl;

import com.bentahsin.benthPinata.configuration.MessageManager;
import com.bentahsin.benthPinata.stats.PlayerStats;
import com.bentahsin.benthPinata.stats.StatsLeaderboardService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class PinataStatsTopCommand {

    private final StatsLeaderboardService leaderboardService;
    private final MessageManager messageManager;

    public PinataStatsTopCommand(StatsLeaderboardService leaderboardService, MessageManager messageManager) {
        this.leaderboardService = leaderboardService;
        this.messageManager = messageManager;
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2 || (!args[1].equalsIgnoreCase("damage") && !args[1].equalsIgnoreCase("kills"))) {
            messageManager.sendMessage(sender, "stats.leaderboard.usage");
            return;
        }

        boolean isDamage = args[1].equalsIgnoreCase("damage");
        List<PlayerStats> topStats = isDamage ? leaderboardService.getTopDamage() : leaderboardService.getTopKills();

        if (topStats.isEmpty()) {
            messageManager.sendMessage(sender, "stats.leaderboard.empty");
            return;
        }

        String titlePath = isDamage ? "stats.leaderboard.header-damage" : "stats.leaderboard.header-kills";
        messageManager.sendMessageList(sender, titlePath);

        for (int i = 0; i < topStats.size(); i++) {
            PlayerStats stats = topStats.get(i);
            UUID playerId = stats.getPlayerId();
            String playerName = Bukkit.getOfflinePlayer(playerId).getName();
            int value = isDamage ? stats.getTotalDamage() : stats.getPinataKills();

            // Eğer oyuncu ismi null ise (profil bulunamadıysa) atla
            if (playerName == null) continue;

            String format = messageManager.getMessage("stats.leaderboard.format");
            String line = format
                    .replace("%rank%", String.valueOf(i + 1))
                    .replace("%player%", playerName)
                    .replace("%value%", String.valueOf(value));

            sender.sendMessage(line);
        }

        messageManager.sendMessageList(sender, "stats.leaderboard.footer");
    }
}