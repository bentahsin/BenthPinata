package com.bentahsin.benthPinata.services;

import com.bentahsin.benthPinata.pinata.model.Pinata;
import com.bentahsin.benthPinata.pinata.model.PinataAbility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.stream.Collectors;

public class AbilityService {

    private final EffectService effectService;

    public AbilityService(EffectService effectService) {
        this.effectService = effectService;
    }

    // Yetenek tetikleme ana mantığı
    public void tryTriggerAbilities(Pinata pinata) {
        double currentHealthPercentage = (double) pinata.getCurrentHealth() * 100 / pinata.getType().getMaxHealth();

        for (PinataAbility ability : pinata.getType().getAbilities()) {
            if (ability.getTrigger().equalsIgnoreCase("HEALTH_BELOW")) {
                if (currentHealthPercentage <= ability.getValue() && !pinata.hasTriggeredAbility(ability)) {
                    executeAbility(pinata, ability);
                }
            }
        }
    }

    // Yeteneği çalıştır
    private void executeAbility(Pinata pinata, PinataAbility ability) {
        pinata.markAbilityAsTriggered(ability);

        // Mesaj ve sesleri oynat
        if (ability.getMessage() != null && !ability.getMessage().isEmpty()) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', ability.getMessage()));
        }
        if (ability.getSound() != null) {
            effectService.playSoundForAll(ability.getSound(), pinata.getEntity().getLocation());
        }

        // Yetenek türüne göre işlem yap
        Collection<Player> nearbyPlayers = getNearbyPlayers(pinata, ability.getRange());

        switch (ability.getType().toUpperCase()) {
            case "SHOCKWAVE":
                for (Player p : nearbyPlayers) {
                    p.setVelocity(p.getLocation().toVector().subtract(pinata.getEntity().getLocation().toVector()).normalize().multiply(ability.getPower()).setY(0.5));
                }
                break;
            case "BLINDING_LIGHT":
                nearbyPlayers.forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, ability.getDuration() * 20, 0)));
                break;
            case "POTION_AURA":
                for (Player p : nearbyPlayers) {
                    ability.getPotionEffects().forEach(effectType ->
                            p.addPotionEffect(new PotionEffect(effectType, ability.getDuration() * 20, ability.getPower())));
                }
                break;
        }
    }

    private Collection<Player> getNearbyPlayers(Pinata pinata, int range) {
        return pinata.getEntity().getNearbyEntities(range, range, range).stream()
                .filter(e -> e instanceof Player)
                .map(e -> (Player) e)
                .collect(Collectors.toList());
    }
}