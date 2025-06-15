package dev.kittycore;

import org.bukkit.plugin.java.JavaPlugin;

import dev.kittycore.command.Balance;
import dev.kittycore.command.Pay;
import dev.kittycore.command.Spawn;

public class App extends JavaPlugin {
    private Economy economy;

    @Override
    public void onEnable() {
        try {
            this.economy = new Economy();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        this.getServer().getPluginManager().registerEvents(new KCListener(this), this);
        this.getCommand("balance").setExecutor(new Balance(this.economy));
        this.getCommand("pay").setExecutor(new Pay(this.economy));
        this.getCommand("pay").setExecutor(new Spawn(this.economy));
    }

    public Economy getEconomy() {
        return this.economy;
    }
}
