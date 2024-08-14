package me.fkpvp;

import me.fkpvp.game.X1;
import me.fkpvp.listener.DefaultPlayerListener;
import me.fkpvp.listener.SoupListener;
import me.fkpvp.listener.X1Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Fkpvp extends JavaPlugin {
    public static Fkpvp instance;
    @Override
    public void onEnable() {
        instance = this;
        this.getServer().getPluginManager().registerEvents(new SoupListener(), this);
        this.getServer().getPluginManager().registerEvents(new DefaultPlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new X1Listener(), this);
        loadKitsConfig();

    }

    public void loadKitsConfig()
    {
        X1.loadKitConfig(this);
    }
}
