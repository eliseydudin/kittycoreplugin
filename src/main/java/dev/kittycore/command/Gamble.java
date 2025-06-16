package dev.kittycore.command;

import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.kittycore.Economy;
import net.md_5.bungee.api.ChatColor;

public class Gamble implements CommandExecutor {
    private Economy instance;

    public Gamble(Economy in) {
        this.instance = in;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command com, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        try {
            long gambled = Long.parseLong(args[0]);

            if (this.instance.getBalance(player.getUniqueId()) < gambled || gambled <= 0) {
                player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "[KC] " + ChatColor.RESET
                        + "insufficient funds :(");
                return true;
            }

            long newGambled = this.instance.gamble(gambled);
            String message;
            if (newGambled == gambled * 7) {
                message = String.format("you've got %d€ from gambling, HELL YEAH", newGambled);
            }
            if (newGambled > 0) {
                message = String.format("you've got %d€ from gambling, hell yeah!", newGambled);
            } else {
                message = String.format("you've lost %d€ from gambling, how sad /j", -newGambled);
            }

            player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "[KC] " + ChatColor.RESET
                    + message);
            this.instance.give(player.getUniqueId(), newGambled - gambled);

        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "[KC] " + ChatColor.RESET
                    + "woah please enter a valid number");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
