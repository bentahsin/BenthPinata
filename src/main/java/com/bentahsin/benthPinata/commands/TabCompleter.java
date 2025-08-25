package com.bentahsin.benthPinata.commands;

import com.bentahsin.benthPinata.pinata.PinataService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * /pinata komutu için TAB ile otomatik tamamlama sağlar.
 * Alt komutları ve /pinata start için Piñata türlerini önerir.
 */
public class TabCompleter implements org.bukkit.command.TabCompleter {

    private static final List<String> SUB_COMMANDS = Arrays.asList("start", "killall", "reload", "help");
    private final PinataService pinataService;

    /**
     * TabCompleter'ı oluşturur ve gerekli servisleri enjekte eder.
     * @param pinataService Piñata türlerinin listesine erişmek için kullanılır.
     */
    public TabCompleter(PinataService pinataService) {
        this.pinataService = pinataService;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // İlk argüman için tamamlama (/pinata <arg1>)
        if (args.length == 1) {
            String currentArg = args[0].toLowerCase();
            return SUB_COMMANDS.stream()
                    // Oyuncunun yazdığı metinle başlayanları filtrele
                    .filter(s -> s.startsWith(currentArg))
                    // Oyuncunun o komutu kullanma yetkisi varsa göster
                    .filter(s -> sender.hasPermission("benthpinata.command." + s))
                    .collect(Collectors.toList());
        }

        // İkinci argüman için tamamlama (/pinata start <arg2>)
        if (args.length == 2) {
            // Eğer ilk komut "start" ise Piñata türlerini öner
            if (args[0].equalsIgnoreCase("start")) {
                // Oyuncunun start komutunu kullanma yetkisi yoksa hiçbir şey önerme
                if (!sender.hasPermission("benthpinata.command.start")) {
                    return Collections.emptyList();
                }

                String currentArg = args[1].toLowerCase();
                Set<String> pinataTypes = pinataService.getLoadedTypeIds();

                return pinataTypes.stream()
                        .filter(type -> type.startsWith(currentArg))
                        .collect(Collectors.toList());
            }
        }

        // Diğer durumlar için hiçbir şey önerme
        return Collections.emptyList();
    }
}