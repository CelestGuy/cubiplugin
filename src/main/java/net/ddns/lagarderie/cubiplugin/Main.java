package net.ddns.lagarderie.cubiplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
    }
    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled !");
    }
}
