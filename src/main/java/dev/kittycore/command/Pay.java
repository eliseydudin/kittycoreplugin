package dev.kittycore.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.kittycore.Economy;
import net.md_5.bungee.api.ChatColor;

public class Pay implements CommandExecutor {
    private Economy econInstance;

    public Pay(Economy econ) {
        this.econInstance = econ;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command com, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        if (args.length != 2) {
            player.sendMessage("uhhh you provided either too much or too little arguments :<");
            return false;
        }

        String player2 = args[0];
        if (player2 == player.getName()) {
            player.sendMessage("you cant give money to yourself lmao");
            return true;
        }
        long price = Long.parseLong(args[1]);

        try {
            long balance = this.econInstance.getBalance(player.getUniqueId());
            if (balance < price) {
                player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "[KC] " + ChatColor.RESET
                        + "insufficient funds :<, your balance is " + balance + "€");
                return true;
            }

            UUID player2id = Bukkit.getPlayer(player2).getUniqueId();
            this.econInstance.give(player2id, price);
            this.econInstance.give(player.getUniqueId(), -price);
            player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "[KC] " + ChatColor.RESET
                    + "paid " + price + "€ to player " + player2);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
