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
            player.sendMessage("uhhh you provided either too many or too few arguments :<");
            return false;
        }

        String player2 = args[0];

        try {
            long price = Long.parseLong(args[1]);

            if (price <= 0) {
                player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "[KC] " + ChatColor.RESET + "the number should be more than 0");
                return true;
            }

            long balance = this.econInstance.getBalance(player.getUniqueId());
            if (balance < price) {
                player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "[KC] " + ChatColor.RESET
                        + "insufficient funds :<, your balance is " + balance + "€");
                return true;
            }

            Player player2Player = Bukkit.getPlayer(player2);
            if (!player2Player.isOnline()) {
                player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "[KC] " + ChatColor.RESET
                        + "player is not online :<");
                return true;
            }
            UUID player2id = player2Player.getUniqueId();
            this.econInstance.give(player2id, price);
            this.econInstance.give(player.getUniqueId(), -price);
            player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "[KC] " + ChatColor.RESET
                    + "paid " + price + "€ to player " + player2);
            player2Player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "[KC] " + ChatColor.RESET
                    + "player " + player.getName() + " sent you " + price + "€");

        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "[KC] " + ChatColor.RESET + "oops cannot parse the number from this command");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } 

        return true;
    }
}
