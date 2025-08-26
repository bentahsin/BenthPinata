package com.bentahsin.benthPinata.stats;

import com.bentahsin.benthPinata.BenthPinata;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Oyuncu istatistiklerini periyodik olarak sıralar ve en iyi oyuncuları önbelleğe alır.
 * Bu, /stats top komutunun her seferinde tüm verileri sıralamasını önleyerek performansı artırır.
 */
public class StatsLeaderboardService {

    private final PlayerStatsService playerStatsService;
    private volatile List<PlayerStats> topDamage = Collections.emptyList();
    private volatile List<PlayerStats> topKills = Collections.emptyList();

    public StatsLeaderboardService(BenthPinata plugin, PlayerStatsService playerStatsService) {
        this.playerStatsService = playerStatsService;
        startUpdateTask(plugin);
    }

    private void startUpdateTask(BenthPinata plugin) {
        // Her 5 dakikada bir asenkron olarak çalışacak bir görev başlat.
        long intervalTicks = TimeUnit.MINUTES.toSeconds(5) * 20;
        new BukkitRunnable() {
            @Override
            public void run() {
                updateLeaderboards();
            }
        }.runTaskTimerAsynchronously(plugin, 20L, intervalTicks); // Başlangıçtan 1 saniye sonra başla
    }

    /**
     * Tüm istatistikleri alır, sıralar ve en iyi 10'u önbelleğe yazar.
     */
    public void updateLeaderboards() {
        List<PlayerStats> allStats = playerStatsService.getAllStats();

        // Hasara göre sırala
        this.topDamage = allStats.stream()
                .filter(stats -> stats.getTotalDamage() > 0)
                .sorted(Comparator.comparingInt(PlayerStats::getTotalDamage).reversed())
                .limit(10)
                .collect(Collectors.toList());

        // Öldürmeye göre sırala
        this.topKills = allStats.stream()
                .filter(stats -> stats.getPinataKills() > 0)
                .sorted(Comparator.comparingInt(PlayerStats::getPinataKills).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * @return Hasara göre sıralanmış en iyi 10 oyuncunun istatistiklerini içeren (önbelleğe alınmış) liste.
     */
    public List<PlayerStats> getTopDamage() {
        return topDamage;
    }

    /**
     * @return Öldürme sayısına göre sıralanmış en iyi 10 oyuncunun istatistiklerini içeren (önbelleğe alınmış) liste.
     */
    public List<PlayerStats> getTopKills() {
        return topKills;
    }
}