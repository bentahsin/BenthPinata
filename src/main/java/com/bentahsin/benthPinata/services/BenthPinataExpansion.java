package com.bentahsin.benthPinata.services;

import com.bentahsin.benthPinata.stats.PlayerStats;
import com.bentahsin.benthPinata.stats.PlayerStatsService;
import com.bentahsin.benthPinata.stats.StatsLeaderboardService;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BenthPinataExpansion extends PlaceholderExpansion {

    private final PlayerStatsService playerStatsService;
    private final StatsLeaderboardService leaderboardService;

    public BenthPinataExpansion(PlayerStatsService playerStatsService, StatsLeaderboardService leaderboardService) {
        this.playerStatsService = playerStatsService;
        this.leaderboardService = leaderboardService;
    }

    @Override
    public @NotNull String getIdentifier() { return "benthpinata"; }

    @Override
    public @NotNull String getAuthor() { return "bentahsin"; }

    @Override
    public @NotNull String getVersion() { return "1.0.0"; }

    @Override
    public boolean persist() { return true; } // Eklenti reload edildiğinde PAPI'nin tekrar hooklaması için

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        // %benthpinata_stats_damage% ve %benthpinata_stats_kills%
        if (params.startsWith("stats_")) {
            if (player == null) return "0";
            PlayerStats stats = playerStatsService.getStats(Objects.requireNonNull(player.getPlayer()));
            if (params.equalsIgnoreCase("stats_damage")) {
                return String.valueOf(stats.getTotalDamage());
            }
            if (params.equalsIgnoreCase("stats_kills")) {
                return String.valueOf(stats.getPinataKills());
            }
        }

        // %benthpinata_top_damage_1_name% veya %benthpinata_top_kills_3_value%
        if (params.startsWith("top_")) {
            String[] parts = params.split("_");
            if (parts.length != 4) return null; // Geçersiz format

            String type = parts[1]; // damage veya kills
            int rank;
            try {
                rank = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                return null; // Geçersiz rank
            }
            String valueType = parts[3]; // name veya value

            if (rank < 1 || rank > 10) return "-"; // 1-10 arası rank destekliyoruz

            List<PlayerStats> leaderboard = type.equalsIgnoreCase("damage")
                    ? leaderboardService.getTopDamage()
                    : leaderboardService.getTopKills();

            if (rank > leaderboard.size()) return "Yok";

            PlayerStats stats = leaderboard.get(rank - 1);
            if (valueType.equalsIgnoreCase("name")) {
                return Bukkit.getOfflinePlayer(stats.getPlayerId()).getName();
            }
            if (valueType.equalsIgnoreCase("value")) {
                return type.equalsIgnoreCase("damage")
                        ? String.valueOf(stats.getTotalDamage())
                        : String.valueOf(stats.getPinataKills());
            }
        }

        return null; // Eşleşen placeholder yoksa null döndür
    }
}