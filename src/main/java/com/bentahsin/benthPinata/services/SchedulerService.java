package com.bentahsin.benthPinata.services;

import com.bentahsin.benthPinata.BenthPinata;
import com.bentahsin.benthPinata.pinata.PinataService;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SchedulerService {

    private final BenthPinata plugin;
    private final PinataService pinataService;
    private final List<ScheduledEventConfig> scheduledEvents = new ArrayList<>();

    public SchedulerService(BenthPinata plugin, PinataService pinataService) {
        this.plugin = plugin;
        this.pinataService = pinataService;
    }

    public void loadAndStart() {
        ConfigurationSection scheduledSection = plugin.getConfigManager().getMainConfig().getConfigurationSection("automatic-events.scheduled");
        if (scheduledSection == null || !scheduledSection.getBoolean("enabled", false)) {
            plugin.getLogger().info("Zamanlanmış etkinlikler (Scheduler) devre dışı.");
            return;
        }

        List<Map<?, ?>> scheduleMaps = scheduledSection.getMapList("schedules");
        for (Map<?, ?> map : scheduleMaps) {
            try {
                String id = (String) map.get("id");
                boolean enabled = (boolean) map.get("enabled");
                if (!enabled) continue; // Kapalıysa yükleme.

                String pinataType = (String) map.get("pinata-type");
                String dayStr = ((String) map.get("day")).toUpperCase();
                String timeStr = (String) map.get("time");
                int minPlayers = (int) map.get("minimum-players");
                List<?> rawAnnounceList = (List<?>) map.get("announce-before");
                List<Integer> announceMinutes = new ArrayList<>();

                if (rawAnnounceList != null) {
                    for (Object obj : rawAnnounceList) {
                        if (obj instanceof Integer) {
                            announceMinutes.add((Integer) obj);
                        } else {
                            plugin.getLogger().warning("Zamanlanmış etkinlik '" + id + "' için 'announce-before' listesinde geçersiz bir değer bulundu: '" + obj + "'. Bu değer atlanıyor.");
                        }
                    }
                }

                DayOfWeek dayOfWeek = null;
                if (!dayStr.equals("EVERYDAY")) {
                    dayOfWeek = DayOfWeek.valueOf(dayStr);
                }

                LocalTime time = LocalTime.parse(timeStr);

                scheduledEvents.add(new ScheduledEventConfig(id, enabled, pinataType, dayOfWeek, time, minPlayers, announceMinutes));
            } catch (Exception e) {
                plugin.getLogger().severe("Zamanlanmış bir etkinlik yüklenirken hata oluştu! Lütfen config.yml'yi kontrol edin. Hata: " + e.getMessage());
            }
        }

        if (!scheduledEvents.isEmpty()) {
            startSchedulerTask();
            plugin.getLogger().info(scheduledEvents.size() + " adet zamanlanmış etkinlik başarıyla yüklendi ve zamanlayıcı başlatıldı.");
        }
    }

    private void startSchedulerTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                LocalDateTime now = LocalDateTime.now();
                DayOfWeek currentDay = now.getDayOfWeek();
                LocalTime currentTime = now.toLocalTime().withSecond(0).withNano(0); // Saniyeleri önemseme

                for (ScheduledEventConfig event : scheduledEvents) {
                    // Gün kontrolü (EVERYDAY veya doğru gün)
                    boolean dayMatches = (event.dayOfWeek() == null || event.dayOfWeek() == currentDay);
                    if (!dayMatches) continue;

                    // Etkinlik başlangıç zamanı kontrolü
                    if (event.time().equals(currentTime)) {
                        tryStartEvent(event);
                        continue; // Aynı dakika içinde duyuru yapmasını engelle
                    }

                    // Duyuru zamanı kontrolü
                    for (int minutesBefore : event.announceBeforeMinutes()) {
                        if (event.time().minusMinutes(minutesBefore).equals(currentTime)) {
                            String message = plugin.getMessageManager().getMessage("scheduled-event-announcement",
                                    "%minutes%", String.valueOf(minutesBefore),
                                    "%pinata_type%", event.pinataType());
                            Bukkit.broadcastMessage(message);
                            break; // Bu etkinlik için başka duyuru kontrol etme
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1200L); // Her 60 saniyede (1 dakika) bir çalıştır
    }

    private void tryStartEvent(ScheduledEventConfig event) {
        if (Bukkit.getOnlinePlayers().size() < event.minimumPlayers()) {
            plugin.getLogger().info("Zamanlanmış etkinlik '" + event.id() + "' minimum oyuncu sayısına (" + event.minimumPlayers() + ") ulaşılamadığı için atlandı.");
            return;
        }

        if (!plugin.getPinataRepository().findAll().isEmpty()) {
            plugin.getLogger().info("Zamanlanmış etkinlik '" + event.id() + "' başka bir Piñata aktif olduğu için atlandı.");
            return;
        }

        String message = plugin.getMessageManager().getMessage("scheduled-event-start", "%pinata_type%", event.pinataType());
        Bukkit.broadcastMessage(message);
        pinataService.startEvent(event.pinataType());
    }
}