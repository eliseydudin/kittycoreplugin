package dev.kittycore;

import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
    private Economy economy;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new KCListener(this), this);
    }

    public Economy getEconomy() {
        return this.economy;
    }
}
