package com.bentahsin.benthPinata.services;

import com.bentahsin.benthPinata.configuration.MessageManager;
import com.bentahsin.benthPinata.pinata.model.Pinata;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BossBarService {
    private final boolean enabled;
    private final BarColor color;
    private final BarStyle style;
    private final Map<UUID, BossBar> activeBossBars = new ConcurrentHashMap<>();
    private final MessageManager messageManager;

    public BossBarService(ConfigurationSection config, MessageManager messageManager) {
        this.enabled = config.getBoolean("enabled", true);
        this.messageManager = messageManager;
        try {
            this.color = BarColor.valueOf(Objects.requireNonNull(config.getString("color", "PINK")).toUpperCase());
            this.style = BarStyle.valueOf(Objects.requireNonNull(config.getString("style", "SOLID")).toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("[BenthPinata] BossBar için geçersiz renk veya stil. Varsayılanlar kullanılıyor.");
            throw new RuntimeException(e);
        }
    }

    public void createBossBar(Pinata pinata) {
        if (!enabled) return;

        String title = messageManager.getMessage("bossbar-title", "%pinata_type%", pinata.getType().getId());
        BossBar bossBar = Bukkit.createBossBar(title, this.color, this.style);
        bossBar.setProgress(1.0);

        for (Player player : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(player);
        }
        activeBossBars.put(pinata.getUniqueId(), bossBar);
    }

    public void updateProgress(Pinata pinata) {
        if (!enabled) return;
        BossBar bossBar = activeBossBars.get(pinata.getUniqueId());
        if (bossBar != null) {
            double progress = (double) pinata.getCurrentHealth() / pinata.getType().getMaxHealth();
            bossBar.setProgress(Math.max(0, progress));
        }
    }

    public void removeBossBar(Pinata pinata) {
        if (!enabled) return;
        BossBar bossBar = activeBossBars.remove(pinata.getUniqueId());
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }

    // Oyuncu sunucuya girdiğinde aktif boss barlara eklemek için
    public void addPlayerToBars(Player player) {
        if (!enabled) return;
        activeBossBars.values().forEach(bar -> bar.addPlayer(player));
    }
}