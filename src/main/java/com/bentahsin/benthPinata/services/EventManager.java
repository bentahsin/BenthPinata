package com.bentahsin.benthPinata.services;

import com.bentahsin.benthPinata.BenthPinata;
import com.bentahsin.benthPinata.configuration.ConfigManager;
import com.bentahsin.benthPinata.pinata.PinataService;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.concurrent.TimeUnit;

public class EventManager implements Listener {

    private final BenthPinata plugin;
    private final PinataService pinataService;
    private final ConfigManager configManager;

    private long lastPlayerCountEventTimestamp = 0;

    public EventManager(BenthPinata plugin, PinataService pinataService, ConfigManager configManager) {
        this.plugin = plugin;
        this.pinataService = pinataService;
        this.configManager = configManager;
    }

    /**
     * Otomatik etkinlik zamanlayıcılarını ve dinleyicilerini başlatır.
     */
    public void start() {
        startTimedEventScheduler();
    }

    private void startTimedEventScheduler() {
        ConfigurationSection timedConfig = configManager.getMainConfig().getConfigurationSection("automatic-events.timed");
        if (timedConfig == null || !timedConfig.getBoolean("enabled", false)) {
            return; // Zamanlı etkinlikler kapalıysa başlatma.
        }

        long intervalTicks = TimeUnit.MINUTES.toSeconds(timedConfig.getInt("interval-minutes", 120)) * 20;

        // Her 5 dakikada bir kontrol eden bir zamanlayıcı.
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            // Sunucudaki oyuncu sayısı minimum gereksinimi karşılıyor mu?
            if (Bukkit.getOnlinePlayers().size() >= timedConfig.getInt("minimum-players", 5)) {

                // Halihazırda aktif bir Piñata var mı? Varsa yenisini başlatma.
                if (plugin.getPinataRepository().findAll().isEmpty()) {
                    String type = timedConfig.getString("pinata-type", "default");
                    Bukkit.broadcastMessage("§d[BenthPiñata] §eSunucu canlandı! Otomatik bir Piñata etkinliği başlıyor!");
                    pinataService.startEvent(type);
                }
            }
        }, intervalTicks, intervalTicks); // Başlangıçta bir kere ve sonra periyodik olarak çalışır.
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ConfigurationSection playerCountConfig = configManager.getMainConfig().getConfigurationSection("automatic-events.player-count");
        if (playerCountConfig == null || !playerCountConfig.getBoolean("enabled", false)) {
            return; // Oyuncu sayısı etkinliği kapalıysa kontrol etme.
        }

        int currentPlayers = Bukkit.getOnlinePlayers().size();
        int requiredPlayers = playerCountConfig.getInt("required-players", 20);

        // Oyuncu sayısı tam olarak hedefe ulaştı mı?
        if (currentPlayers == requiredPlayers) {

            // Cooldown süresi geçti mi?
            long cooldownMillis = TimeUnit.MINUTES.toMillis(playerCountConfig.getInt("cooldown-minutes", 90));
            if (System.currentTimeMillis() - lastPlayerCountEventTimestamp > cooldownMillis) {

                // Halihazırda aktif bir Piñata var mı?
                if (plugin.getPinataRepository().findAll().isEmpty()) {
                    lastPlayerCountEventTimestamp = System.currentTimeMillis();
                    String type = playerCountConfig.getString("pinata-type", "default");
                    Bukkit.broadcastMessage("§d[BenthPinata] §bSunucu dolup taşıyor! §6" + requiredPlayers + " §boyuncuya ulaşıldığı için özel bir Piñata etkinliği başlıyor!");
                    pinataService.startEvent(type);
                }
            }
        }
    }
}