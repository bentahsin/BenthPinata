package com.bentahsin.benthPinata.commands.impl;

import com.bentahsin.benthPinata.commands.ISubCommand;
import com.bentahsin.benthPinata.configuration.MessageManager;
import com.bentahsin.benthPinata.pinata.PinataService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PinataStartCommand implements ISubCommand {

    private final PinataService pinataService;
    private final MessageManager messageManager;

    public PinataStartCommand(PinataService pinataService, MessageManager messageManager) {
        this.pinataService = pinataService;
        this.messageManager = messageManager;
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getPermission() {
        return "benthpinata.command.start";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            messageManager.sendMessage(sender, "start-command-usage");
            return;
        }

        String pinataTypeName = args[0];
        Location customLocation = null;

        // Komut formatını kontrol et
        if (args.length > 1) {
            // Durum 1: /pinata start <tür> here
            if (args.length == 2 && args[1].equalsIgnoreCase("here")) {
                if (!(sender instanceof Player)) { // DEĞİŞİKLİK 1
                    messageManager.sendMessage(sender, "player-only-command");
                    return;
                }
                Player player = (Player) sender; // DEĞİŞİKLİK 1
                customLocation = player.getLocation();
            }
            // Durum 2: /pinata start <tür> <dünya> <x> <y> <z>
            else if (args.length == 5) {
                try {
                    World world = Bukkit.getWorld(args[1]);
                    if (world == null) {
                        messageManager.sendMessage(sender, "invalid-world", "%world%", args[1]);
                        return;
                    }
                    double x = Double.parseDouble(args[2]);
                    double y = Double.parseDouble(args[3]);
                    double z = Double.parseDouble(args[4]);
                    customLocation = new Location(world, x, y, z);
                } catch (NumberFormatException e) {
                    messageManager.sendMessage(sender, "invalid-coordinates");
                    return;
                }
            } else {
                messageManager.sendMessage(sender, "start-command-usage");
                return;
            }
        }

        // PinataService'i uygun metotla çağır
        boolean success = pinataService.startEvent(pinataTypeName, customLocation);

        if (!success) {
            messageManager.sendMessage(sender, "pinata-type-not-found");
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        // /pinata start <tür>
        if (args.length == 1) {
            String currentArg = args[0].toLowerCase();
            return pinataService.getLoadedTypeIds().stream()
                    .filter(type -> type.toLowerCase().startsWith(currentArg))
                    .collect(Collectors.toList());
        }

        // /pinata start <tür> [here | dünya]
        if (args.length == 2) {
            List<String> suggestions = new ArrayList<>();
            if (sender instanceof Player) {
                suggestions.add("here");
            }
            // DEĞİŞİKLİK 2: .toList() yerine .collect(Collectors.toList()) kullanıldı
            List<String> worldNames = Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
            suggestions.addAll(worldNames);

            return suggestions.stream()
                    .filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        // /pinata start <tür> <dünya> [x] [y] [z]
        if (args.length >= 3 && args.length <= 5) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Location loc = player.getLocation();
                if (args.length == 3) return Collections.singletonList(String.valueOf(loc.getBlockX()));
                if (args.length == 4) return Collections.singletonList(String.valueOf(loc.getBlockY()));
                if (args.length == 5) return Collections.singletonList(String.valueOf(loc.getBlockZ()));
            }
        }

        return Collections.emptyList();
    }
}