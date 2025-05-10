package dev.kittycore;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class KCListener implements Listener {
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "WELKOM IN EUROPA JONGEN!");
        }
    }
}
