package com.bentahsin.benthPinata.configuration;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * messages.yml dosyasındaki tüm metinleri yönetir. Renklendirme,
 * placeholder değiştirme ve mesaj gönderme işlemlerini merkezileştirir.
 */
public class MessageManager {

    private final ConfigManager configManager;

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public MessageManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    /**
     * Hem standart (&) hem de HEX (&#RRGGBB) renk kodlarını çeviren metot.
     * 1.13 API'si ile uyumludur ve 1.16+ sunucularda HEX renklerini gösterir.
     * @param text Çevrilecek metin.
     * @return Renklendirilmiş metin.
     */
    private String translateColorCodes(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            // #RRGGBB'yi bul ve onu &x&R&R&G&G&B&B formatına çevir.
            String hexColor = matcher.group(1);
            StringBuilder magic = new StringBuilder("&x");
            for (char c : hexColor.toCharArray()) {
                magic.append('&').append(c);
            }
            matcher.appendReplacement(buffer, magic.toString());
        }
        matcher.appendTail(buffer);

        // Son olarak, standart '&' renk kodlarını çevir.
        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
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
                if (message != null) {
                    message = message.replace(placeholders[i], placeholders[i + 1]);
                }
            }
        }
        return translateColorCodes(message);
    }

    /**
     * Belirtilen yoldaki mesaj listesini alır ve renklendirir.
     * @param path messages.yml içindeki listenin yolu.
     * @return İşlenmiş mesaj listesi.
     */
    public List<String> getMessageList(String path) {
        return configManager.getMessagesConfig().getStringList(path).stream()
                .map(this::translateColorCodes)
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