package com.bentahsin.benthPinata.commands;

import com.bentahsin.benthPinata.configuration.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Ana /pinata komutunu yönetir ve gelen argümanlara göre
 * ilgili alt komut (ISubCommand) sınıfını çalıştırır.
 * Eğer hiçbir argüman girilmezse, varsayılan olarak 'help' komutunu çalıştırır.
 */
public class CommandManager implements CommandExecutor {

    private final MessageManager messageManager;
    private final Map<String, ISubCommand> subCommands = new HashMap<>();

    public CommandManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    /**
     * Komut yöneticisine yeni bir alt komut kaydeder.
     * @param subCommand Kaydedilecek ISubCommand nesnesi.
     */
    public void registerCommand(ISubCommand subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Eğer oyuncu hiçbir argüman girmemişse (/pinata), yardım komutunu çalıştır.
        if (args.length == 0) {
            ISubCommand helpCommand = subCommands.get("help");
            if (helpCommand != null) {
                // Yardım komutunun da yetkisini kontrol etmeliyiz.
                if (helpCommand.getPermission() != null && !sender.hasPermission(helpCommand.getPermission())) {
                    messageManager.sendMessage(sender, "no-permission");
                } else {
                    helpCommand.execute(sender, new String[0]); // Argüman olmadan çalıştır
                }
            } else {
                // 'help' komutu bir şekilde kaydedilmemişse, genel kullanım hatası mesajı gönder.
                messageManager.sendMessage(sender, "invalid-command-usage");
            }
            return true;
        }

        // Bir alt komut belirtilmişse onu bul ve çalıştır.
        String subCommandName = args[0].toLowerCase();
        ISubCommand subCommand = subCommands.get(subCommandName);

        if (subCommand == null) {
            messageManager.sendMessage(sender, "invalid-command-usage");
            return true;
        }

        // Komut için yetki kontrolü yap.
        if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) {
            messageManager.sendMessage(sender, "no-permission");
            return true;
        }

        // Argüman dizisini, alt komutun adını içermeyecek şekilde yeniden oluştur.
        // Örn: ["start", "default"] -> ["default"]
        String[] subCommandArgs = Arrays.copyOfRange(args, 1, args.length);

        subCommand.execute(sender, subCommandArgs);
        return true;
    }
}