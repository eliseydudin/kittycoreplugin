package dev.kittycore;

import java.sql.SQLException;
//import java.util.List;

import org.bukkit.ChatColor;
//import org.bukkit.Material;
import org.bukkit.World;
//import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
//import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
//import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
//import org.bukkit.metadata.MetadataValue;

public class KCListener implements Listener {
    private App handle;

    public KCListener(App handle) {
        this.handle = handle;
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Economy econ = this.handle.getEconomy();

        try {
            if (!player.hasPlayedBefore() || !econ.userExists(player.getUniqueId())) {
                econ.createUser(player.getUniqueId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "WELKOM IN EUROPA JONGEN!");
        World world = this.handle.getServer().getWorlds().get(0);
        long time = world.getTime();

        long hours = (time / 1000 + 6) % 24;
        long minutes = ((time % 1000) * 60 / 1000) % 60;

        try {
            String messages[] = {
                    String.format("current world time: %d:%d", hours, minutes),
                    String.format("your balance: %d€", econ.getBalance(player.getUniqueId()))
            };
            player.sendMessage(messages);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void OnKill(PlayerDeathEvent event) {
        Player killed = event.getEntity();
        Player killer = killed.getKiller();

        if (killer == null) {
            return;
        }

        try {
            long worth = this.handle.getEconomy().getWorth(killed.getUniqueId());
            this.handle.getEconomy().give(killer.getUniqueId(), worth);
            killer.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "[KC] " + ChatColor.RESET
                    + "you have received " + worth + "€ for killing a player");

            this.handle.getEconomy().give(killed.getUniqueId(), -worth);
            killed.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "[KC] " + ChatColor.RESET
                    + "you have lost " + worth + "€ for getting killed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // @EventHandler
    // public void OnChestInteract(PlayerInteractEvent event) {
    // if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
    // return;
    // }

    // Block block = event.getClickedBlock();

    // if (!block.getType().equals(Material.ENDER_CHEST)) {
    // return;
    // }

    // List<MetadataValue> metadata = block.getMetadata("null");
    // if (metadata.isEmpty()) {
    // return;
    // }

    // event.setCancelled(true);
    // MetadataValue meta = metadata.get(metadata.size() - 1);
    // }
}
