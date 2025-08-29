package com.bentahsin.benthPinata.stats;

import org.bukkit.Bukkit;
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

    public PlayerStatsService(JavaPlugin plugin) {
        this.plugin = plugin;
        this.statsFile = new File(plugin.getDataFolder(), "stats.yml");
        try {
            if (!statsFile.exists() && statsFile.createNewFile()) {
                plugin.getLogger().info("stats.yml dosyası oluşturuldu.");
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "stats.yml dosyası oluşturulamadı!", e);
        }
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

   /**
    * İstatistikleri dosyadan asenkron olarak yükler.
    * Bu, sunucu başlangıcında yaşanacak olası lag'ı önler.
    */
    public void loadStatsAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            FileConfiguration statsConfig = YamlConfiguration.loadConfiguration(statsFile);
            Map<UUID, PlayerStats> loadedStats = new HashMap<>();

            if (statsConfig.isConfigurationSection("stats")) {
                Objects.requireNonNull(statsConfig.getConfigurationSection("stats")).getKeys(false).forEach(uuidString -> {
                    try {
                        UUID playerId = UUID.fromString(uuidString);
                        PlayerStats stats = new PlayerStats(playerId);
                        stats.setTotalDamage(statsConfig.getInt("stats." + uuidString + ".totalDamage"));
                        stats.setPinataKills(statsConfig.getInt("stats." + uuidString + ".pinataKills"));
                        loadedStats.put(playerId, stats);
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("stats.yml içinde geçersiz UUID formatı: " + uuidString);
                    }
                });
            }

            // Yüklenen veriyi ana thread'e güvenli bir şekilde aktar
            Bukkit.getScheduler().runTask(plugin, () -> {
                statsMap.clear();
                statsMap.putAll(loadedStats);
                plugin.getLogger().info(loadedStats.size() + " oyuncu istatistiği asenkron olarak yüklendi.");
            });
        });
    }

  /**
   * Tüm istatistikleri dosyaya asenkron olarak kaydeder.
   */
    public void saveStatsAsync() {
        Map<UUID, PlayerStats> statsToSave = new HashMap<>(statsMap);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            FileConfiguration statsConfig = new YamlConfiguration();
            for (Map.Entry<UUID, PlayerStats> entry : statsToSave.entrySet()) {
                String uuidString = entry.getKey().toString();
                statsConfig.set("stats." + uuidString + ".totalDamage", entry.getValue().getTotalDamage());
                statsConfig.set("stats." + uuidString + ".pinataKills", entry.getValue().getPinataKills());
            }
            try {
                statsConfig.save(statsFile);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "İstatistikler dosyaya kaydedilemedi!", e);
            }
        });
    }
    
    /**
     * Sunucu kapanırken kullanılacak senkron kaydetme metodu.
     */
    public void saveStatsSync() {
        FileConfiguration statsConfig = new YamlConfiguration();
        for (Map.Entry<UUID, PlayerStats> entry : statsMap.entrySet()) {
            String uuidString = entry.getKey().toString();
            statsConfig.set("stats." + uuidString + ".totalDamage", entry.getValue().getTotalDamage());
            statsConfig.set("stats." + uuidString + ".pinataKills", entry.getValue().getPinataKills());
        }
        try {
            statsConfig.save(statsFile);
            plugin.getLogger().info("Oyuncu istatistikleri kaydedildi.");
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "İstatistikler dosyaya kaydedilemedi!", e);
        }
    }

    public List<PlayerStats> getAllStats() {
        return new ArrayList<>(statsMap.values());
    }

   /**
    * Bir oyuncunun istatistiklerini verimli bir şekilde sıfırlar ve dosyayı asenkron günceller.
    * @param playerId Sıfırlanacak oyuncunun UUID'si.
    */
   public void resetPlayerStats(UUID playerId) {
       // 1. Bellekten anında kaldır.
       statsMap.remove(playerId);

       // 2. Dosyadan kaldırma işlemini asenkron olarak yap.
       Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
           FileConfiguration statsConfig = YamlConfiguration.loadConfiguration(statsFile);
           // Sadece ilgili oyuncunun bölümünü null yap, tüm dosyayı silme.
           statsConfig.set("stats." + playerId.toString(), null);
           try {
               statsConfig.save(statsFile);
           } catch (IOException e) {
               plugin.getLogger().log(Level.SEVERE, "Oyuncu istatistiği sıfırlanırken dosya kaydedilemedi!", e);
           }
       });
   }

   /**
    * Tüm istatistikleri sıfırlar ve dosyayı asenkron günceller.
    */
    public void resetAllStats() {
        statsMap.clear();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            FileConfiguration statsConfig = YamlConfiguration.loadConfiguration(statsFile);
            statsConfig.set("stats", null); // Tüm 'stats' bölümünü kaldır.
            try {
                statsConfig.save(statsFile);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Tüm istatistikler sıfırlanırken dosya kaydedilemedi!", e);
            }
        });
    }
}