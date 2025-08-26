package com.bentahsin.benthPinata.pinata;

import com.bentahsin.benthPinata.BenthPinata;
import com.bentahsin.benthPinata.configuration.MessageManager;
import com.bentahsin.benthPinata.configuration.SettingsManager;
import com.bentahsin.benthPinata.pinata.model.Pinata;
import com.bentahsin.benthPinata.pinata.model.PinataAbility;
import com.bentahsin.benthPinata.pinata.model.PinataType;
import com.bentahsin.benthPinata.services.*;
import com.bentahsin.benthPinata.stats.PlayerStatsService;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Piñata'ların tüm yaşam döngüsünü yöneten ana servis.
 * Geri sayım, doğma, hasar alma, ölme ve temizleme işlemlerini yürütür.
 */
public class PinataService {

    private final BenthPinata plugin;
    private final SettingsManager settingsManager;
    private final MessageManager messageManager;
    private final PinataRepository pinataRepository;
    private final HologramService hologramService;
    private final EffectService effectService;
    private final RewardService rewardService;
    private final PlaceholderService placeholderService;
    private final BossBarService bossBarService;
    private final AbilityService abilityService;
    private final PlayerStatsService playerStatsService;

    private final Map<String, PinataType> loadedPinataTypes = new HashMap<>();
    private final List<BukkitTask> activeTasks = new ArrayList<>();
    private final Map<UUID, Long> playerCooldowns = new ConcurrentHashMap<>();

    public PinataService(BenthPinata plugin, SettingsManager settingsManager, MessageManager messageManager,
                         PinataRepository pinataRepository, HologramService hologramService,
                         EffectService effectService, RewardService rewardService, PlaceholderService placeholderService,
                         BossBarService bossBarService, AbilityService abilityService,
                         PlayerStatsService playerStatsService) {
        this.plugin = plugin;
        this.settingsManager = settingsManager;
        this.messageManager = messageManager;
        this.pinataRepository = pinataRepository;
        this.hologramService = hologramService;
        this.effectService = effectService;
        this.rewardService = rewardService;
        this.placeholderService = placeholderService;
        this.bossBarService = bossBarService;
        this.abilityService = abilityService;
        this.playerStatsService = playerStatsService;
    }

    /**
     * Eklenti açılırken veya yeniden yüklenirken config'den tüm Piñata türlerini okur ve belleğe yükler.
     * Bu metot artık daha sağlam ve hata ayıklama için daha fazla bilgi sağlar.
     */
    public void loadPinataTypes() {
        loadedPinataTypes.clear();
        FileConfiguration mainConfig = plugin.getConfigManager().getMainConfig();
        FileConfiguration abilitiesConfig = plugin.getConfigManager().getAbilitiesConfig();

        if (abilitiesConfig == null) {
            plugin.getLogger().severe("Abilities config (abilities.yml) yüklenemedi! Yetenekler devre dışı kalacak.");
            return;
        }

        String typesPath = "pinata-types";
        ConfigurationSection pinataTypesSection = mainConfig.getConfigurationSection(typesPath);

        if (pinataTypesSection == null) {
            plugin.getLogger().warning("config.yml içinde 'pinata-types' bölümü bulunamadı. Hiçbir Piñata türü yüklenmedi.");
            return;
        }

        Set<String> typeIds = pinataTypesSection.getKeys(false);

        for (String id : typeIds) {
            String locationString = mainConfig.getString(typesPath + "." + id + ".spawn-location");
            Location spawnLocation = parseLocation(locationString);
            if (spawnLocation == null) {
                plugin.getLogger().warning(id + " adlı Piñata türü için konum geçersiz. Bu tür yüklenmedi.");
                continue;
            }
            int health = mainConfig.getInt(typesPath + "." + id + ".health", settingsManager.getDefaultPinataHealth());

            List<PinataAbility> abilities = new ArrayList<>();

            if (abilitiesConfig.isSet(id)) {
                List<Map<?, ?>> abilityMaps = abilitiesConfig.getMapList(id);

                if (abilityMaps.isEmpty()) {
                    plugin.getLogger().warning("'" + id + "' için abilities.yml dosyasında bir bölüm bulundu, ancak içi boş veya hatalı biçimlendirilmiş. Yetenek yüklenmedi.");
                }

                for (Map<?, ?> map : abilityMaps) {
                    try {
                        String type = (String) map.get("type");
                        String trigger = (String) map.get("trigger");

                        int value = getIntFromMap(map, "value", 0);
                        int range = getIntFromMap(map, "range", 10);
                        int power = getIntFromMap(map, "power", 1);
                        int duration = getIntFromMap(map, "duration", 5);
                        String message = (String) map.get("message");
                        String sound = (String) map.get("sound");

                        List<PotionEffectType> potions = new ArrayList<>();
                        Object rawPotionList = map.get("potion-effects");
                        if (rawPotionList instanceof List) {
                            for (Object rawEffect : (List<?>) rawPotionList) {
                                if (rawEffect instanceof String) {
                                    PotionEffectType effectType = PotionEffectType.getByName((String) rawEffect);
                                    if (effectType != null) {
                                        potions.add(effectType);
                                    } else {
                                        plugin.getLogger().warning(id + " türü için yetenekte geçersiz iksir efekti: " + rawEffect);
                                    }
                                }
                            }
                        }
                        abilities.add(new PinataAbility(type, trigger, value, range, power, duration, message, sound, potions));
                    } catch (Exception e) {
                        plugin.getLogger().severe(id + " türü için bir yetenek yüklenirken hata oluştu: " + e.getMessage());
                    }
                }
            }

            PinataType pinataType = new PinataType(id, spawnLocation, health, abilities);
            loadedPinataTypes.put(id.toLowerCase(), pinataType);
            plugin.getLogger().info(id + " adlı Piñata türü " + abilities.size() + " yetenek ile yüklendi.");
        }
    }

    private int getIntFromMap(Map<?, ?> map, String key, int defaultValue) {
        Object obj = map.get(key);
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        return defaultValue;
    }

    /**
     * Belirtilen türde bir Piñata etkinliği için geri sayımı başlatır.
     * @param typeName Başlatılacak Piñata'nın tür ID'si.
     * @return Başlatma başarılıysa true, tür bulunamazsa false.
     */
    public boolean startEvent(String typeName) {
        Optional<PinataType> typeOpt = getPinataType(typeName);
        if (typeOpt.isEmpty()) {
            return false;
        }
        PinataType type = typeOpt.get();

        broadcastTitle("countdown-started");
        effectService.playSoundForAll("countdown-start", type.spawnLocation());

        int countdownTime = settingsManager.getCountdownTime();
        final int[] remainingTime = {countdownTime};

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (remainingTime[0] < 1) {
                spawnPinata(type);
                return;
            }
            broadcastTitle("countdown-title", "%time%", String.valueOf(remainingTime[0]));
            effectService.playSoundForAll("countdown-tick", type.spawnLocation());
            remainingTime[0]--;
        }, 0L, 20L);

        // Görevin süresi bittiğinde kendini otomatik olarak iptal etmesi için
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!task.isCancelled()) {
                task.cancel();
                activeTasks.remove(task);
            }
        }, (countdownTime + 1) * 20L);

        activeTasks.add(task);
        return true;
    }

    /**
     * Bir oyuncu Piñata'ya vurduğunda tetiklenir.
     * @param damager Hasarı vuran oyuncu.
     * @param pinata Hasar alan Piñata.
     */
    public void handleDamage(Player damager, Pinata pinata) {
        long now = System.currentTimeMillis();
        long cooldownEnd = playerCooldowns.getOrDefault(damager.getUniqueId(), 0L);
        if (now < cooldownEnd) return;
        playerCooldowns.put(damager.getUniqueId(), now + settingsManager.getHitCooldownMillis());

        boolean isDead = pinata.applyDamage(damager, 1);

        playerStatsService.addDamage(damager, 1);

        // Hasar sonrası tüm servisleri bilgilendir
        abilityService.tryTriggerAbilities(pinata);
        bossBarService.updateProgress(pinata);
        hologramService.updateHologramFor(pinata);
        effectService.playSoundForAll("hit", pinata.getEntity().getLocation());
        effectService.spawnParticle("hit", pinata.getEntity().getEyeLocation());

        int totalDamage = pinata.getDamagers().getOrDefault(damager.getUniqueId(), 0);
        sendHitActionBar(damager,
                "%health%", String.valueOf(pinata.getCurrentHealth()),
                "%total_damage%", String.valueOf(totalDamage));

        rewardService.giveOnHitReward(damager);
        rewardService.checkAndGrantThresholdRewards(damager, pinata);

        if (isDead) {
            handleDeath(pinata);
        }
    }

    /**
     * Tüm aktif Piñata'ları ve ilgili görevleri sonlandırır.
     */
    public void killAll() {
        // Aktif görevleri iptal et ve listeyi temizle
        new ArrayList<>(activeTasks).forEach(BukkitTask::cancel);
        activeTasks.clear();

        // Aktif Piñata'ları sil
        new ArrayList<>(pinataRepository.findAll()).forEach(this::cleanupPinata);
        pinataRepository.clear();

        // Bu, bir sonraki etkinliğin temiz başlamasını garantiler.
        playerCooldowns.clear();
    }

    /**
     * Bir Piñata türünün var olup olmadığını kontrol eder.
     * @param id Türün ID'si.
     * @return PinataType nesnesini içeren bir Optional.
     */
    public Optional<PinataType> getPinataType(String id) {
        return Optional.ofNullable(loadedPinataTypes.get(id.toLowerCase()));
    }


    // --- Private Helper Methods ---

    /**
     * Dünyada bir Piñata oluşturur ve onu yönetmeye başlar.
     * @param type Oluşturulacak Piñata'nın türü.
     */
    private void spawnPinata(PinataType type) {
        Location loc = type.spawnLocation();
        Sheep sheep = (Sheep) Objects.requireNonNull(loc.getWorld()).spawnEntity(loc, EntityType.SHEEP);

        // Entity ayarları
        sheep.setInvulnerable(false);
        sheep.setCollidable(false);
        sheep.setAI(true);
        sheep.setCustomNameVisible(false);
        sheep.setRemoveWhenFarAway(false);
        sheep.setMetadata(Pinata.METADATA_KEY, new FixedMetadataValue(plugin, type.id()));

        Pinata pinata = new Pinata(type, sheep);
        hologramService.createHologramFor(pinata);
        pinataRepository.save(pinata);
        bossBarService.createBossBar(pinata);

        broadcastTitle("pinata-spawned");
        effectService.playSoundForAll("spawn", sheep.getLocation());

        // Kendi kendini iptal edebilen bir task oluşturmak için referans tutucu
        final BukkitTask[] updateTaskHolder = new BukkitTask[1];

        BukkitTask updateTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            // Eğer Piñata entity'si artık geçerli değilse...
            if (!pinata.getEntity().isValid()) {
                // Görevin kendisini güvenli bir şekilde iptal et.
                if (updateTaskHolder[0] != null) {
                    updateTaskHolder[0].cancel();
                    activeTasks.remove(updateTaskHolder[0]);
                }
                return;
            }

            // Normal güncelleme mantığı
            pinata.getEntity().setColor(DyeColor.values()[new Random().nextInt(DyeColor.values().length)]);
            hologramService.updateHologramFor(pinata);
            abilityService.tryTriggerAbilities(pinata);

            // Rastgele bir oyuncuyu hedef al
            List<Player> nearbyPlayers = pinata.getEntity().getNearbyEntities(30, 30, 30)
                    .stream()
                    .filter(e -> e instanceof Player)
                    .map(e -> (Player) e)
                    .toList();
            if (!nearbyPlayers.isEmpty()) {
                pinata.getEntity().setTarget(nearbyPlayers.get(new Random().nextInt(nearbyPlayers.size())));
            }

        }, 20L, 40L);

        // Task referansını hem tutucuya hem de Piñata nesnesine ata
        updateTaskHolder[0] = updateTask;
        pinata.setUpdateTask(updateTask);
        activeTasks.add(updateTask);
    }

    /**
     * Bir Piñata öldüğünde çalışır, ödülleri dağıtır ve temizlik yapar.
     * @param pinata Ölen Piñata.
     */
    public void handleDeath(Pinata pinata) {
        effectService.playSoundForAll("death", pinata.getEntity().getLocation());
        effectService.spawnParticle("death", pinata.getEntity().getLocation());
        broadcastTitle("pinata-death-title");

        // Broadcast mesajları için placeholder'ları doldurup gönder
        List<Map.Entry<UUID, Integer>> sortedDamagers = pinata.getSortedDamagers();

        if (!sortedDamagers.isEmpty()) {
            UUID topDamagerId = sortedDamagers.get(0).getKey();
            Player topDamager = Bukkit.getPlayer(topDamagerId);
            if (topDamager != null && topDamager.isOnline()) {
                playerStatsService.addKill(topDamager);
            }
        }

        messageManager.getMessageList("death-broadcast").forEach(line -> {
            String processedLine = placeholderService.parseTopDamagers(line, sortedDamagers);
            Bukkit.broadcastMessage(processedLine);
        });

        rewardService.giveFinalRewards(sortedDamagers);
        cleanupPinata(pinata);
    }

    /**
     * Bir Piñata ile ilişkili her şeyi (hologram, entity) sunucudan kaldırır.
     * @param pinata Temizlenecek Piñata.
     */
    private void cleanupPinata(Pinata pinata) {
        if (pinata == null) return;

        if (pinata.getUpdateTask() != null && !pinata.getUpdateTask().isCancelled()) {
            pinata.getUpdateTask().cancel();
            activeTasks.remove(pinata.getUpdateTask());
        }

        bossBarService.removeBossBar(pinata);
        hologramService.deleteHologramFor(pinata);
        if (pinata.getEntity() != null) {
            pinata.getEntity().remove();
        }
        pinataRepository.remove(pinata);
    }

    private void broadcastTitle(String path, String... placeholders) {
        String combined = messageManager.getMessage(path, placeholders);
        String[] parts = combined.split(";", 2);
        String title = parts[0];
        String subtitle = parts.length > 1 ? parts[1] : "";

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(title, subtitle, 10, 70, 20);
        }
    }

    private void sendHitActionBar(Player player, String... placeholders) {
        String message = messageManager.getMessage("actionbar-hit", placeholders);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    private Location parseLocation(String locStr) {
        if (locStr == null || locStr.isEmpty()) return null;
        try {
            String[] parts = locStr.split(";");
            World world = Bukkit.getWorld(parts[0]);
            if (world == null) return null;
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            return new Location(world, x, y, z);
        } catch (Exception e) {
            plugin.getLogger().severe("Konum formatı hatalı: " + locStr);
            return null;
        }
    }

    /**
     * Belleğe yüklenmiş olan tüm Piñata türlerinin ID'lerini döndürür.
     * @return Piñata türü ID'lerini içeren bir Set.
     */
    public Set<String> getLoadedTypeIds() {
        return loadedPinataTypes.keySet();
    }
}