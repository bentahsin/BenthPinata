package com.bentahsin.benthPinata;

import com.bentahsin.benthPinata.commands.CommandManager;
import com.bentahsin.benthPinata.commands.TabCompleter;
import com.bentahsin.benthPinata.commands.impl.PinataHelpCommand;
import com.bentahsin.benthPinata.commands.impl.PinataKillAllCommand;
import com.bentahsin.benthPinata.commands.impl.PinataReloadCommand;
import com.bentahsin.benthPinata.commands.impl.PinataStartCommand;
import com.bentahsin.benthPinata.configuration.ConfigManager;
import com.bentahsin.benthPinata.configuration.MessageManager;
import com.bentahsin.benthPinata.configuration.SettingsManager;
import com.bentahsin.benthPinata.listeners.PinataInteractionListener;
import com.bentahsin.benthPinata.pinata.PinataRepository;
import com.bentahsin.benthPinata.pinata.PinataService;
import com.bentahsin.benthPinata.services.*;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * BenthPinata eklentisinin ana sınıfı.
 * Eklenti yaşam döngüsünü yönetir, servisleri başlatır ve
 * komutlar ile olay dinleyicilerini kaydeder.
 */
public final class BenthPinata extends JavaPlugin {

    private ConfigManager configManager;
    private SettingsManager settingsManager;
    private MessageManager messageManager;
    private HologramService hologramService;
    private EffectService effectService;
    private RewardService rewardService;
    private PlaceholderService placeholderService;
    private BossBarService bossBarService;
    private AbilityService abilityService;
    private PinataRepository pinataRepository;
    private PinataService pinataService;
    private EventManager eventManager;

    @Override
    public void onEnable() {
        // 1. Konfigürasyon yöneticisini başlat
        this.configManager = new ConfigManager(this);

        // 2. Ayar ve Mesaj yöneticilerini, konfigürasyon yöneticisine bağla
        this.settingsManager = new SettingsManager(configManager);
        this.messageManager = new MessageManager(configManager);

        // 3. Bağımsız servisleri oluştur
        this.placeholderService = new PlaceholderService();
        this.effectService = new EffectService(configManager);
        this.hologramService = new HologramService(messageManager);

        // 4. BossBar servisini başlat
        this.bossBarService = new BossBarService(Objects.requireNonNull(configManager.getMainConfig().getConfigurationSection("boss-bar")));

        // 5. Diğer servislere bağımlı olan servisleri oluştur
        this.rewardService = new RewardService(configManager, placeholderService);
        this.abilityService = new AbilityService(effectService);

        // 6. Depo ve Ana Servis katmanlarını oluştur
        this.pinataRepository = new PinataRepository();
        this.pinataService = new PinataService(
                this,
                settingsManager,
                messageManager,
                pinataRepository,
                hologramService,
                effectService,
                rewardService,
                placeholderService,
                bossBarService,
                abilityService
        );

        // 7. EventManager'ı, pinataService oluşturulduktan sonra başlat
        this.eventManager = new EventManager(this, this.pinataService, this.configManager);
        this.eventManager.start();

        // 8. Eklenti başlarken Piñata türlerini config'den yükle
        this.pinataService.loadPinataTypes();

        // 9. Komutları ve Listener'ları kaydet
        startup();

        getLogger().info("BenthPinata eklentisi yeni mimari ile başarıyla başlatıldı!");
    }

    @Override
    public void onDisable() {
        shutdown();
        getLogger().info("BenthPinata eklentisi devre dışı bırakıldı.");
    }

    /**
     * Eklentiyi başlatan veya yeniden yükleyen ana mantık.
     * Tüm servisleri oluşturur ve olay dinleyicilerini kaydeder.
     */
    public void startup() {
        // 1. Konfigürasyon yöneticisini başlat
        this.configManager = new ConfigManager(this);
        this.configManager.setup();

        // 2. Servisleri ve yöneticileri oluştur
        this.settingsManager = new SettingsManager(configManager);
        this.messageManager = new MessageManager(configManager);
        this.placeholderService = new PlaceholderService();
        this.effectService = new EffectService(configManager);
        this.hologramService = new HologramService(messageManager);
        this.bossBarService = new BossBarService(Objects.requireNonNull(configManager.getMainConfig().getConfigurationSection("boss-bar")));
        this.rewardService = new RewardService(configManager, placeholderService);
        this.abilityService = new AbilityService(effectService);
        this.pinataRepository = new PinataRepository();
        this.pinataService = new PinataService(this, settingsManager, messageManager, pinataRepository, hologramService, effectService, rewardService, placeholderService, bossBarService, abilityService);
        this.eventManager = new EventManager(this, this.pinataService, this.configManager);
        this.eventManager.start();

        // 3. Piñata türlerini yükle
        this.pinataService.loadPinataTypes();

        // 4. Komutları ve Olay Dinleyicilerini kaydet
        registerHandlers();
    }

    /**
     * Eklentiyle ilgili tüm aktif işlemleri durdurur ve durumu temizler.
     */
    public void shutdown() {
        if (this.pinataService != null) {
            this.pinataService.killAll();
        }
        HandlerList.unregisterAll(this);
    }

    /**
     * Komut Yöneticisini ve Olay Dinleyicilerini başlatır ve kaydeder.
     * Bu metodu public yaptık.
     */
    public void registerHandlers() {
        CommandManager commandManager = new CommandManager(this.messageManager);
        commandManager.registerCommand(new PinataStartCommand(this.pinataService, this.messageManager));
        commandManager.registerCommand(new PinataReloadCommand(this));
        commandManager.registerCommand(new PinataKillAllCommand(this.pinataService, this.messageManager));
        commandManager.registerCommand(new PinataHelpCommand(this.messageManager));
        Objects.requireNonNull(getCommand("pinata")).setExecutor(commandManager);
        Objects.requireNonNull(getCommand("pinata")).setTabCompleter(new TabCompleter(this.pinataService));

        // Listener'ları Kaydet
        getServer().getPluginManager().registerEvents(new PinataInteractionListener(this.pinataRepository, this.pinataService, this.bossBarService), this);
        getServer().getPluginManager().registerEvents(this.eventManager, this);
    }

    /**
     * Eklentinin tüm yapılandırmasını ve bağımlı servislerini yeniden yükler.
     * Bu metot, durum sızıntılarını (state leaks) önlemek için tasarlanmıştır.
     */
    public void reload() {
        shutdown();
        startup();
        getLogger().info("BenthPinata eklentisi başarıyla yeniden yüklendi.");
    }

    public ConfigManager getConfigManager() { return configManager; }
    public MessageManager getMessageManager() { return messageManager; }
    public PinataRepository getPinataRepository() { return pinataRepository; }
}