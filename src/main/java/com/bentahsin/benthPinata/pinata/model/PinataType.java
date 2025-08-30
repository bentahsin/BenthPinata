package com.bentahsin.benthPinata.pinata.model;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Bir Piñata türünün yapılandırma bilgilerini tutan değişmez (immutable) bir veri sınıfı.
 * Eklenti başlangıcında config'den okunarak oluşturulur.
 */
public final class PinataType {

    private final String id;
    private final Location spawnLocation;
    private final int maxHealth;
    private final List<PinataAbility> abilities;
    private final EntityType entityType;
    private final Map<String, Object> mobOptions;

    public PinataType(String id, Location spawnLocation, int maxHealth, List<PinataAbility> abilities, EntityType entityType, Map<String, Object> mobOptions) {
        this.id = id;
        this.spawnLocation = spawnLocation;
        this.maxHealth = maxHealth;
        this.abilities = abilities;
        this.entityType = entityType;
        this.mobOptions = mobOptions;
    }

    public String getId() {
        return id;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public List<PinataAbility> getAbilities() {
        return abilities;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Map<String, Object> getMobOptions() {
        return mobOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PinataType that = (PinataType) o;
        return maxHealth == that.maxHealth &&
                Objects.equals(id, that.id) &&
                Objects.equals(spawnLocation, that.spawnLocation) &&
                Objects.equals(abilities, that.abilities) &&
                entityType == that.entityType &&
                Objects.equals(mobOptions, that.mobOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, spawnLocation, maxHealth, abilities, entityType, mobOptions);
    }

    @Override
    public String toString() {
        return "PinataType{" +
                "id='" + id + '\'' +
                ", spawnLocation=" + spawnLocation +
                ", maxHealth=" + maxHealth +
                ", abilities=" + abilities +
                ", entityType=" + entityType +
                ", mobOptions=" + mobOptions +
                '}';
    }
}