package com.bentahsin.benthPinata.listeners;

import com.bentahsin.benthPinata.pinata.PinataRepository;
import com.bentahsin.benthPinata.pinata.PinataService;
import com.bentahsin.benthPinata.pinata.model.Pinata;
import com.bentahsin.benthPinata.services.BossBarService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

public class PinataInteractionListener implements Listener {

    private final PinataRepository pinataRepository;
    private final PinataService pinataService;
    private final BossBarService bossBarService;

    public PinataInteractionListener(PinataRepository pinataRepository, PinataService pinataService, BossBarService bossBarService) {
        this.pinataRepository = pinataRepository;
        this.pinataService = pinataService;
        this.bossBarService = bossBarService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPinataDamageByPlayer(EntityDamageByEntityEvent event) {
        Optional<Pinata> pinataOpt = pinataRepository.findByEntity(event.getEntity());

        // Eğer vurulan bir Piñata ise, vanilla hasarını her zaman iptal et.
        if (pinataOpt.isPresent()) {
            event.setCancelled(true);
        }

        // "Eğer vuran bir Player ise (adı damager olsun) VE vurulan bir Pinata ise..."
        if (event.getDamager() instanceof Player damager && pinataOpt.isPresent()) {
            Pinata pinata = pinataOpt.get();
            pinataService.handleDamage(damager, pinata);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPinataEnvironmentDamage(EntityDamageEvent event) {
        // Eğer bu hasar zaten yukarıdaki metot tarafından yönetiliyorsa (yani bir oyuncu vuruşuysa),
        // bu metodun bir şey yapmasına gerek yok. O yüzden bu tür olayları atlıyoruz.
        if (event instanceof EntityDamageByEntityEvent) {
            return;
        }

        // Hasar alan varlığın bir Piñata olup olmadığını kontrol et.
        Optional<Pinata> pinataOpt = pinataRepository.findByEntity(event.getEntity());

        // Eğer hasar alan bir Piñata ise ve hasarın nedeni oyuncu vuruşu değilse
        // (düşme, boğulma, yanma vb.), olayı iptal et.
        if (pinataOpt.isPresent()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPinataDeath(EntityDeathEvent event) {
        Optional<Pinata> pinataOpt = pinataRepository.findByEntity(event.getEntity());
        if (pinataOpt.isPresent()) {
            event.getDrops().clear();
            event.setDroppedExp(0);
            pinataService.handleDeath(pinataOpt.get());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        bossBarService.addPlayerToBars(event.getPlayer());
    }
}