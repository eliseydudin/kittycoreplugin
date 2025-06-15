package dev.kittycore.command;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.kittycore.Economy;
import net.md_5.bungee.api.ChatColor;

public class Spawn implements CommandExecutor {
    private Economy instance;

    public Spawn(Economy in) {
        this.instance = in;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command com, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getLogger().log(Level.WARNING, "oops cannot teleport player to spawn!");
            return false;
        }

        Player player = (Player) sender;

        try {
            if (this.instance.getBalance(player.getUniqueId()) < 10) {
                player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "[KC] " + ChatColor.RESET
                        + "insufficient funds :(");
                return true;
            }
            this.instance.give(player.getUniqueId(), -10);
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                    String.format("mvtp %s world", player.getName()));
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                    String.format("mvspawn %s", player.getName()));
            player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "[KC] " + ChatColor.RESET
                    + "teleported ya to spawn!");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
