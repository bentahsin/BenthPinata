package com.bentahsin.benthPinata.configuration;

import com.bentahsin.benthPinata.BenthPinata;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Birden fazla YAML yapılandırma dosyasını yönetmek için merkezi bir sınıf.
 * Eklentinin veri klasöründeki dosyaları yükler, okur ve kaydeder.
 */
public class ConfigManager {

    private final BenthPinata plugin;
    private FileConfiguration mainConfig;
    private FileConfiguration messagesConfig;
    private FileConfiguration rewardsConfig;
    private FileConfiguration abilitiesConfig;

    public ConfigManager(BenthPinata plugin) {
        this.plugin = plugin;
        setup();
    }

    /**
     * Gerekli tüm yapılandırma dosyalarını oluşturur ve yükler.
     */
    public void setup() {
        File mainConfigFile = createAndLoadFile("config.yml");
        File messagesConfigFile = createAndLoadFile("messages.yml");
        File rewardsConfigFile = createAndLoadFile("rewards.yml");
        File abilitiesConfigFile = createAndLoadFile("abilities.yml");

        mainConfig = YamlConfiguration.loadConfiguration(mainConfigFile);
        messagesConfig = YamlConfiguration.loadConfiguration(messagesConfigFile);
        rewardsConfig = YamlConfiguration.loadConfiguration(rewardsConfigFile);
        abilitiesConfig = YamlConfiguration.loadConfiguration(abilitiesConfigFile);
    }

    /**
     * Belirtilen dosyayı eklenti veri klasöründe oluşturur (eğer yoksa)
     * ve File nesnesini döndürür.
     * @param fileName Oluşturulacak dosyanın adı.
     * @return Oluşturulan dosyanın File nesnesi.
     */
    private File createAndLoadFile(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        return file;
    }

    // İlgili config nesneleri için getter metotları
    public FileConfiguration getMainConfig() {
        return mainConfig;
    }

    public FileConfiguration getMessagesConfig() {
        return messagesConfig;
    }

    public FileConfiguration getRewardsConfig() {
        return rewardsConfig;
    }

    public FileConfiguration getAbilitiesConfig() { return abilitiesConfig; }
    }