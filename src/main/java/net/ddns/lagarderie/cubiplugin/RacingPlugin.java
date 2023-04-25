package net.ddns.lagarderie.cubiplugin;

import net.ddns.lagarderie.cubiplugin.commands.CommandCheckpoint;
import net.ddns.lagarderie.cubiplugin.commands.CommandChildren;
import net.ddns.lagarderie.cubiplugin.commands.CommandGame;
import net.ddns.lagarderie.cubiplugin.commands.CommandTrack;
import net.ddns.lagarderie.cubiplugin.modes.RacingMode;
import net.ddns.lagarderie.cubiplugin.game.Racing;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

import static net.ddns.lagarderie.cubiplugin.utils.TrackUtils.saveTracks;

public class RacingPlugin extends JavaPlugin {
    private static RacingPlugin instance = null;
    private final HashMap<String, RacingMode> modes = new HashMap<>();

    public RacingPlugin() {
        instance = this;
    }

    public static RacingPlugin getInstance() {
        if (instance == null) {
            instance = new RacingPlugin();
        }
        return instance;
    }

    public void onLoad() {
        Thread.currentThread().setContextClassLoader(this.getClassLoader());

        if (new File("./plugins/racing").mkdir()) {
            getLogger().log(Level.INFO, "Création du dossier de configuration du plugin");
        }

        if (new File("./plugins/racing/tracks").mkdir()) {
            getLogger().log(Level.INFO, "Création du dossier de courses");
        }
    }

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("game")).setExecutor(new CommandGame());
        Objects.requireNonNull(getCommand("track")).setExecutor(new CommandTrack());
        Objects.requireNonNull(getCommand("checkpoint")).setExecutor(new CommandCheckpoint());
        Objects.requireNonNull(getCommand("children")).setExecutor(new CommandChildren());
    }

    @Override
    public void onDisable() {
        saveTracks(Racing.tracks);
    }

    public boolean startMode(String playerName, RacingMode mode) {
        RacingMode actualMode = modes.get(playerName);

        if (actualMode != mode) {
            if (actualMode != null) {
                actualMode.stop();
            }

            modes.put(playerName, mode);
            mode.start();
            return true;
        }

        return false;
    }

    public boolean stopMode(String playerName, RacingMode mode) {
        mode.stop();
        modes.remove(playerName);

        return true;
    }

    public RacingMode getMode(String playerName) {
        return modes.get(playerName);
    }
}
