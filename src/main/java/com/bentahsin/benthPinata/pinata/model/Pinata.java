package com.bentahsin.benthPinata.pinata.model;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Aktif bir Piñata'nın anlık durumunu temsil eden veri sınıfı.
 * Canı, hasar vuranları ve dünyadaki entity'si gibi bilgileri içerir.
 */
public class Pinata {

    public static final String METADATA_KEY = "benthpinata_id";
    private final UUID uniqueId;
    private final PinataType type;
    private final LivingEntity entity;
    private Hologram hologram;
    private BukkitTask updateTask;
    private int currentHealth;
    private final Map<UUID, Integer> damagers;
    private final Map<UUID, Set<String>> claimedThresholds = new ConcurrentHashMap<>();
    private final Set<PinataAbility> triggeredAbilities = new HashSet<>();

    public Pinata(PinataType type, LivingEntity entity) {
        this.uniqueId = UUID.randomUUID();
        this.type = type;
        this.entity = entity;
        this.currentHealth = type.maxHealth();
        this.damagers = new HashMap<>();
    }

    /**
     * Piñata'ya hasar uygular ve canını azaltır.
     * @param player Hasarı vuran oyuncu.
     * @param amount Hasar miktarı.
     * @return Piñata'nın ölüp ölmediğini döndürür (true eğer öldüyse).
     */
    public boolean applyDamage(Player player, int amount) {
        this.currentHealth -= amount;
        damagers.merge(player.getUniqueId(), amount, Integer::sum);
        return this.currentHealth <= 0;
    }

    private boolean isDying = false;

    public boolean isDying() {
        return isDying;
    }

    public void setDying(boolean dying) {
        isDying = dying;
    }

    /**
     * Hasar listesini en çok vurandan en aza doğru sıralanmış olarak döndürür.
     * @return Sıralanmış Map.Entry listesi.
     */
    public List<Map.Entry<UUID, Integer>> getSortedDamagers() {
        return damagers.entrySet().stream()
                .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
    }

    /**
     * Bir oyuncunun belirli bir eşik ödülünü daha önce alıp almadığını kontrol eder.
     * @param playerId Oyuncunun UUID'si.
     * @param thresholdId Ödülün eşsiz ID'si (rewards.yml'deki).
     * @return Eğer ödül daha önce alınmışsa true.
     */
    public boolean hasClaimedThreshold(UUID playerId, String thresholdId) {
        return claimedThresholds.getOrDefault(playerId, Collections.emptySet()).contains(thresholdId);
    }

    /**
     * Bir oyuncu için bir eşik ödülünü "alındı" olarak işaretler.
     * @param playerId Oyuncunun UUID'si.
     * @param thresholdId Ödülün eşsiz ID'si.
     */
    public void markThresholdAsClaimed(UUID playerId, String thresholdId) {
        // Eğer oyuncu için daha önce bir set oluşturulmamışsa, yeni bir tane oluşturur.
        claimedThresholds.computeIfAbsent(playerId, k -> new HashSet<>()).add(thresholdId);
    }

    public boolean hasTriggeredAbility(PinataAbility ability) {
        return triggeredAbilities.contains(ability);
    }

    public void markAbilityAsTriggered(PinataAbility ability) {
        triggeredAbilities.add(ability);
    }

    // Getters and Setters
    public UUID getUniqueId() { return uniqueId; }
    public PinataType getType() { return type; }
    public LivingEntity getEntity() { return entity; }
    public int getCurrentHealth() { return currentHealth; }
    public Map<UUID, Integer> getDamagers() { return damagers; }
    public BukkitTask getUpdateTask() { return updateTask; }
    public void setUpdateTask(BukkitTask updateTask) { this.updateTask = updateTask; }
    public Hologram getHologram() { return hologram; }
    public void setHologram(Hologram hologram) { this.hologram = hologram; }
}