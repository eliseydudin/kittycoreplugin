package dev.kittycore;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.google.common.collect.ImmutableList;

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

    @EventHandler
    public void OnPlayerAchievment(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();

        try {
            this.handle.getEconomy().give(player.getUniqueId(), 50);
            player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "[KC] " + ChatColor.RESET
                    + "you have received 50€ for doing an achievement");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void OnPlayerMessage(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();

        event.setCancelled(true);
        boolean global = (event.getMessage().startsWith("!"));

        for (Player player : ImmutableList.copyOf(Bukkit.getServer().getOnlinePlayers())) {
            try {
                if (global || player.getLocation().distance(sender.getLocation()) <= 30) {
                    player.sendMessage(
                            this.colorOfPlayer(sender) + "[" + sender.getName() + "]" + ChatColor.RESET + " "
                                    + event.getMessage());
                }

            } catch (Exception e) {
                /* different worlds */
                if (global) {
                    player.sendMessage(
                            this.colorOfPlayer(sender) + "[" + sender.getName() + "]" + ChatColor.RESET + " "
                                    + event.getMessage());
                }
            }
        }

    }

    private ChatColor colorOfPlayer(Player p) {
        ChatColor[] colors = {
                ChatColor.RED,
                ChatColor.GREEN,
                ChatColor.BLUE,
                ChatColor.LIGHT_PURPLE,
                ChatColor.AQUA,
                ChatColor.GOLD,
        };

        long colorAsInt = Math.abs(p.getUniqueId().getMostSignificantBits()) % colors.length;
        return colors[(int) colorAsInt];
    }
}
