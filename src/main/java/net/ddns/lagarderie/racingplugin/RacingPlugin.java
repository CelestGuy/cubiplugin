package net.ddns.lagarderie.racingplugin;

import net.ddns.lagarderie.racingplugin.commands.*;
import net.ddns.lagarderie.racingplugin.game.PowerBlock;
import net.ddns.lagarderie.racingplugin.game.PowerBlocksManager;
import net.ddns.lagarderie.racingplugin.game.RacingGame;
import net.ddns.lagarderie.racingplugin.game.Track;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

import static net.ddns.lagarderie.racingplugin.utils.RacingGameUtils.*;
import static org.bukkit.Bukkit.getPluginManager;

public class RacingPlugin extends JavaPlugin {
    private static final String name = "racingplugin";
    private ArrayList<Track> tracks;

    private final PowerBlocksManager powerBlocksManager = PowerBlocksManager.getInstance();

    public static RacingPlugin getPlugin() {
        return (RacingPlugin) getPluginManager().getPlugin(name);
    }

    public void onLoad() {
        tracks = loadTracks();

        loadGameParameters();

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
        Objects.requireNonNull(getCommand("showcheckpoints")).setExecutor(new CommandShowcheckpoints());
        Objects.requireNonNull(getCommand("checkpoint")).setExecutor(new CommandCheckpoint());
        Objects.requireNonNull(getCommand("children")).setExecutor(new CommandChildren());
        Objects.requireNonNull(getCommand("powerblock")).setExecutor(new CommandPowerBlock());
    }

    @Override
    public void onDisable() {
        saveTracks(tracks);
        savePowerBlocks();
        saveGameParameters();
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }
}
