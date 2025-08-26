package com.bentahsin.benthPinata.stats;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class PlayerStatsService {
    private final Map<UUID, PlayerStats> statsMap = new ConcurrentHashMap<>();
    private final JavaPlugin plugin;
    private final File statsFile;
    private final FileConfiguration statsConfig;

    public PlayerStatsService(JavaPlugin plugin) {
        this.plugin = plugin;
        this.statsFile = new File(plugin.getDataFolder(), "stats.yml");
        if (!statsFile.exists()) {
            try {
                if (statsFile.createNewFile()) {
                    plugin.getLogger().info("stats.yml dosyası oluşturuldu.");
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "stats.yml dosyası oluşturulamadı!", e);
            }
        }
        this.statsConfig = YamlConfiguration.loadConfiguration(statsFile);
    }

    public PlayerStats getStats(Player player) {
        return statsMap.computeIfAbsent(player.getUniqueId(), PlayerStats::new);
    }

    public void addDamage(Player player, int amount) {
        getStats(player).addDamage(amount);
    }

    public void addKill(Player player) {
        getStats(player).addKill();
    }

    public void loadStats() {
        if (statsConfig.getConfigurationSection("stats") == null) {
            return;
        }
        Objects.requireNonNull(statsConfig.getConfigurationSection("stats")).getKeys(false).forEach(uuidString -> {
            try {
                UUID playerId = UUID.fromString(uuidString);
                PlayerStats stats = new PlayerStats(playerId);
                stats.setTotalDamage(statsConfig.getInt("stats." + uuidString + ".totalDamage"));
                stats.setPinataKills(statsConfig.getInt("stats." + uuidString + ".pinataKills"));
                statsMap.put(playerId, stats);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("stats.yml içinde geçersiz UUID formatı bulundu ve atlandı: " + uuidString);
            }
        });
        plugin.getLogger().info("Oyuncu istatistikleri yüklendi.");
    }

    public void saveStats() {
        for (Map.Entry<UUID, PlayerStats> entry : statsMap.entrySet()) {
            String uuidString = entry.getKey().toString();
            statsConfig.set("stats." + uuidString + ".totalDamage", entry.getValue().getTotalDamage());
            statsConfig.set("stats." + uuidString + ".pinataKills", entry.getValue().getPinataKills());
        }
        try {
            statsConfig.save(statsFile);
            plugin.getLogger().info("Oyuncu istatistikleri kaydedildi.");
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "İstatistikler dosyaya kaydedilemedi: " + statsFile.getName(), e);
        }
    }

    public List<PlayerStats> getAllStats() {
        return new ArrayList<>(statsMap.values());
    }

    /**
     * İYİLEŞTİRME: Bu metot artık kod tekrarı yapmak yerine merkezi saveStats() metodunu çağırıyor.
     * Bu, hem kodu temizler hem de üçüncü uyarıyı ortadan kaldırır.
     */
    public void resetPlayerStats(UUID playerId) {
        statsMap.remove(playerId);
        statsConfig.set("stats." + playerId.toString(), null);
        saveStats();
    }

    public void resetAllStats() {
        statsMap.clear();
        statsConfig.set("stats", null);
        saveStats();
    }
}