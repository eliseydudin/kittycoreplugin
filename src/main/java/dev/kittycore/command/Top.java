package dev.kittycore.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.kittycore.Economy;
import dev.kittycore.Pair;

public class Top implements CommandExecutor {
    private Economy instance;

    public Top(Economy econ) {
        this.instance = econ;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command com, String label, String[] args) {
        try {
            int counter = 1;
            for (Pair<UUID, Long> data : this.instance.getTopFive()) {
                Player p = Bukkit.getPlayer(data.getKey());
                String name;

                if (p == null) {
                    OfflinePlayer pl = Bukkit.getOfflinePlayer(data.getKey());
                    name = pl.getName();
                } else {
                    name = p.getName();
                }

                sender.sendMessage(counter + ". " + name + " - " + data.getValue() + "€");
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
