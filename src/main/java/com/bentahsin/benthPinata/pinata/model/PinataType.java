package com.bentahsin.benthPinata.pinata.model;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Map;

/**
 * Bir Piñata türünün yapılandırma bilgilerini tutan değişmez (immutable) bir veri sınıfı.
 * Eklenti başlangıcında config'den okunarak oluşturulur.
 */
public record PinataType(
        String id,
        Location spawnLocation,
        int maxHealth,
        List<PinataAbility> abilities,
        EntityType entityType,
        Map<String, Object> mobOptions
) { }