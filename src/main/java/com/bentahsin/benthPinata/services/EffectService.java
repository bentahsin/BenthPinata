package com.bentahsin.benthPinata.services;

import com.bentahsin.benthPinata.configuration.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

/**
 * Eklenti içindeki tüm ses ve partikül efektlerini yönetir.
 */
public class EffectService {

    private final ConfigManager configManager;

    public EffectService(ConfigManager configManager) {
        this.configManager = configManager;
    }

    /**
     * Tüm oyunculara belirli bir konfigürasyon yolundaki sesi çalar.
     * @param soundPath config.yml içindeki sesin yolu.
     * @param location Sesin çalınacağı konum.
     */
    public void playSoundForAll(String soundPath, Location location) {
        try {
            String soundName = configManager.getMainConfig().getString("sounds." + soundPath);
            Sound sound = Sound.valueOf(soundName.toUpperCase());
            location.getWorld().playSound(location, sound, 1.0f, 1.0f);
        } catch (Exception e) {
            Bukkit.getLogger().warning("[BenthPinata] 'sounds." + soundPath + "' yolundaki ses geçersiz.");
        }
    }

    /**
     * Belirli bir konumda partikül efekti oluşturur.
     * @param particlePath config.yml içindeki partikülün yolu.
     * @param location Efektin oluşturulacağı konum.
     */
    public void spawnParticle(String particlePath, Location location) {
        try {
            String particleName = configManager.getMainConfig().getString("effects." + particlePath);
            Particle particle = Particle.valueOf(particleName.toUpperCase());
            location.getWorld().spawnParticle(particle, location, 30, 0.5, 0.5, 0.5, 0.05);
        } catch (Exception e) {
            Bukkit.getLogger().warning("[BenthPinata] 'effects." + particlePath + "' yolundaki partikül geçersiz.");
        }
    }
}