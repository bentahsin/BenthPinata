package com.bentahsin.benthPinata.pinata.model;

import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class PinataAbility {
    private final String type;
    private final String trigger;
    private final int value;
    private final int range;
    private final int power;
    private final int duration;
    private final String message;
    private final String sound;
    private final List<PotionEffectType> potionEffects;

    public PinataAbility(String type, String trigger, int value, int range, int power, int duration, String message, String sound, List<PotionEffectType> potionEffects) {
        this.type = type;
        this.trigger = trigger;
        this.value = value;
        this.range = range;
        this.power = power;
        this.duration = duration;
        this.message = message;
        this.sound = sound;
        this.potionEffects = potionEffects;
    }

    public String getType() { return type; }
    public String getTrigger() { return trigger; }
    public Integer getValue() { return value; }
    public Integer getRange() { return range; }
    public Integer getPower() { return power; }
    public Integer getDuration() { return duration; }
    public String getMessage() { return message; }
    public List<PotionEffectType> getPotionEffects() { return potionEffects; }
    public String getSound() { return sound; }
}