package com.bentahsin.benthPinata.services;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Metinlerdeki özel placeholder'ları (%cp_top_name_1% gibi) gerçek verilerle değiştirir.
 */
public class PlaceholderService {

    private static final Pattern TOP_DAMAGER_PATTERN = Pattern.compile("%bp_top_(name|dmg)_(\\d+)%");
    private static final String DEFAULT_NAME = "Yok";
    private static final String DEFAULT_DMG = "-";

    public String parseTopDamagers(String text, List<Map.Entry<UUID, Integer>> sortedDamagers) {
        Matcher matcher = TOP_DAMAGER_PATTERN.matcher(text);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String type = matcher.group(1); // "name" veya "dmg"
            int rank = Integer.parseInt(matcher.group(2)); // 1, 2, 3...

            String replacement; // Değeri başta atama
            if (rank > 0 && rank <= sortedDamagers.size()) {
                Map.Entry<UUID, Integer> entry = sortedDamagers.get(rank - 1);
                if ("name".equals(type)) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(entry.getKey());
                    replacement = player.getName() != null ? player.getName() : "Bilinmiyor";
                } else { // "dmg"
                    replacement = entry.getValue().toString();
                }
            } else {
                // Eğer o sırada oyuncu yoksa varsayılan değerleri kullan
                replacement = "name".equals(type) ? DEFAULT_NAME : DEFAULT_DMG;
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}