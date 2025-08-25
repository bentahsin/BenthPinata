package com.bentahsin.benthPinata.pinata.model;

import org.bukkit.Location;

import java.util.List;

/**
 * Bir Piñata türünün yapılandırma bilgilerini tutan değişmez (immutable) bir veri sınıfı.
 * Eklenti başlangıcında config'den okunarak oluşturulur.
 */
public record PinataType(String id, Location spawnLocation, int maxHealth, List<PinataAbility> abilities) { }