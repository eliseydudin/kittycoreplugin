package dev.kittycore.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.kittycore.Economy;
import javafx.util.Pair;

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

                sender.sendMessage(counter + ". " + p.getName() + " - " + data.getValue() + "€");
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
