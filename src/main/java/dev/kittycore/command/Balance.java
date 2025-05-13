package dev.kittycore.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.kittycore.Economy;
import net.md_5.bungee.api.ChatColor;

public class Balance implements CommandExecutor {
    private Economy econInstance;

    public Balance(Economy econ) {
        this.econInstance = econ;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command com, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        try {
            long balance = this.econInstance.getBalance(player.getUniqueId());
            player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "[KC] " + ChatColor.RESET
                    + "your current balance is: " + balance + "€");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
