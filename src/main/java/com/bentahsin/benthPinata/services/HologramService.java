package com.bentahsin.benthPinata.services;

import com.bentahsin.benthPinata.configuration.MessageManager;
import com.bentahsin.benthPinata.pinata.model.Pinata;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DecentHolograms API'si ile Piñata hologramlarını yönetir.
 */
public class HologramService {

    private final MessageManager messageManager;

    public HologramService(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    public void createHologramFor(Pinata pinata) {
        String hologramName = "pinata-" + pinata.getUniqueId().toString();
        Location hologramLocation = getHologramLocation(pinata.getEntity().getLocation());

        Hologram hologram = DHAPI.createHologram(hologramName, hologramLocation, getHologramLines(pinata));
        pinata.setHologram(hologram);
    }

    public void updateHologramFor(Pinata pinata) {
        if (pinata.getHologram() == null) return;

        DHAPI.setHologramLines(pinata.getHologram(), getHologramLines(pinata));
        DHAPI.moveHologram(pinata.getHologram(), getHologramLocation(pinata.getEntity().getLocation()));
    }

    public void deleteHologramFor(Pinata pinata) {
        if (pinata.getHologram() != null) {
            pinata.getHologram().delete();
        }
    }

    private List<String> getHologramLines(Pinata pinata) {
        return messageManager.getMessageList("hologram.lines").stream()
                .map(line -> line.replace("%health%", String.valueOf(pinata.getCurrentHealth())))
                .map(line -> line.replace("%max_health%", String.valueOf(pinata.getType().getMaxHealth())))
                .collect(Collectors.toList());
    }

    private Location getHologramLocation(Location entityLocation) {
        return entityLocation.clone().add(0, 2.2, 0);
    }
}