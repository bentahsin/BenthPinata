package com.bentahsin.benthPinata.configuration;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * config.yml dosyasındaki ayarlara kolay ve tip-güvenli erişim sağlar.
 * Kod içindeki hard-coded path'leri ve tip dönüşümlerini önler.
 */
public class SettingsManager {

    private final ConfigManager configManager;
    private int countdownTime;
    private int defaultPinataHealth;
    private int hitCooldownMillis;

    public SettingsManager(ConfigManager configManager) {
        this.configManager = configManager;
        loadSettings();
    }

    /**
     * Ayarları config dosyasından yükler ve sınıf değişkenlerine atar.
     */
    public void loadSettings() {
        FileConfiguration config = configManager.getMainConfig();
        this.countdownTime = config.getInt("countdown-time", 15);
        this.defaultPinataHealth = config.getInt("default-pinata-health", 100);
        this.hitCooldownMillis = config.getInt("general.hit-cooldown-millis", 500);
    }

    public int getCountdownTime() { return countdownTime; }
    public int getDefaultPinataHealth() { return defaultPinataHealth; }
    public int getHitCooldownMillis() { return hitCooldownMillis; }
}