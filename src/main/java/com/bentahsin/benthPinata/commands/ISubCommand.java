package com.bentahsin.benthPinata.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Tüm alt komutların uygulaması gereken arayüz (interface).
 * Bu yapı, komut yöneticisinin modüler olmasını sağlar.
 */
public interface ISubCommand {

    /**
     * @return Alt komutun adı (örn: "start", "reload").
     */
    String getName();

    /**
     * @return Komutu kullanmak için gereken yetki (permission).
     */
    String getPermission();

    /**
     * Komut çalıştırıldığında yürütülecek olan mantık.
     * @param sender Komutu çalıştıran kişi (Player veya Console).
     * @param args Ana komuttan sonra gelen argümanlar.
     */
    void execute(CommandSender sender, String[] args);

    List<String> tabComplete(CommandSender sender, String[] args);
}