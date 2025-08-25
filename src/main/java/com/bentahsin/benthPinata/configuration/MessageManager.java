package com.bentahsin.benthPinata.configuration;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

/**
 * messages.yml dosyasındaki tüm metinleri yönetir. Renklendirme,
 * placeholder değiştirme ve mesaj gönderme işlemlerini merkezileştirir.
 */
public class MessageManager {

    private final ConfigManager configManager;

    public MessageManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    /**
     * Belirtilen yoldaki mesajı alır, renklendirir ve döndürür.
     * @param path messages.yml içindeki mesajın yolu.
     * @param placeholders Değiştirilecek placeholderlar (örn: "%player%", "Bentahsin")
     * @return İşlenmiş mesaj.
     */
    public String getMessage(String path, String... placeholders) {
        String message = configManager.getMessagesConfig().getString(path, "&cMesaj bulunamadı: " + path);

        for (int i = 0; i < placeholders.length; i += 2) {
            if (i + 1 < placeholders.length) {
                message = message.replace(placeholders[i], placeholders[i + 1]);
            }
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Belirtilen yoldaki mesaj listesini alır ve renklendirir.
     * @param path messages.yml içindeki listenin yolu.
     * @return İşlenmiş mesaj listesi.
     */
    public List<String> getMessageList(String path) {
        return configManager.getMessagesConfig().getStringList(path).stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
    }

    /**
     * Bir oyuncuya veya konsola config'den bir mesaj gönderir.
     * @param sender Alıcı (Player veya Console).
     * @param path Mesajın yolu.
     * @param placeholders Değiştirilecek placeholderlar.
     */
    public void sendMessage(CommandSender sender, String path, String... placeholders) {
        sender.sendMessage(getMessage(path, placeholders));
    }

    /**
     * Bir oyuncuya veya konsola config'den bir mesaj listesi gönderir.
     * @param sender Alıcı.
     * @param path Mesaj listesinin yolu.
     */
    public void sendMessageList(CommandSender sender, String path) {
        for (String line : getMessageList(path)) {
            sender.sendMessage(line);
        }
    }
}