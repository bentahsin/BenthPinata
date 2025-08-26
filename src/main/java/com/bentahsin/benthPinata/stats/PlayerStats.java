package com.bentahsin.benthPinata.stats;

import java.util.UUID;

public class PlayerStats {
    private final UUID playerId;
    private int totalDamage;
    private int pinataKills;

    public PlayerStats(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getPlayerId() { return playerId; }
    public int getTotalDamage() { return totalDamage; }
    public int getPinataKills() { return pinataKills; }

    public void setTotalDamage(int totalDamage) { this.totalDamage = totalDamage; }
    public void setPinataKills(int pinataKills) { this.pinataKills = pinataKills; }

    public void addDamage(int amount) { totalDamage += amount; }
    public void addKill() { pinataKills++; }
}