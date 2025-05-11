package dev.kittycore;

import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class KCListener implements Listener {
    private App handle;

    public KCListener(App handle) {
        this.handle = handle;
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "WELKOM IN EUROPA JONGEN!");
        }

        World world = this.handle.getServer().getWorlds().get(0);
        long time = world.getTime();

        long hours = (time / 1000 + 6) % 24;
        long minutes = ((time % 1000) * 60 / 1000) % 60;

        try {
            String messages[] = {
                    String.format("current world time: %d:%d", hours, minutes),
                    String.format("your balance: %d$", this.handle.getEconomy().getBalance(player.getUniqueId()))
            };
            player.sendMessage(messages);
        } catch (SQLException e) {
            this.handle.getLogger().log(Level.WARNING, "cannot fetch user's balance");
            this.handle.getLogger().log(Level.WARNING, e.getStackTrace().toString());
        }

    }
}
