package net.ddns.lagarderie.cubiplugin;

import net.ddns.lagarderie.cubiplugin.commands.CommandInfos;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("infos")).setExecutor(new CommandInfos());
    }
    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled !");
    }
}
