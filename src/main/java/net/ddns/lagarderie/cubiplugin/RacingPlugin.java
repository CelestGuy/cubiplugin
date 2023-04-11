package net.ddns.lagarderie.cubiplugin;

import net.ddns.lagarderie.cubiplugin.commands.CommandCheckpoint;
import net.ddns.lagarderie.cubiplugin.commands.CommandTrack;
import net.ddns.lagarderie.cubiplugin.game.TrackDebugMode;
import net.ddns.lagarderie.cubiplugin.game.Racing;
import net.ddns.lagarderie.cubiplugin.game.Track;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

public class RacingPlugin extends JavaPlugin {
    private static RacingPlugin instance = null;
    private static Racing racing = null;
    private final ArrayList<Track> tracks = new ArrayList<>();
    private final TrackDebugMode trackDebugMode = new TrackDebugMode();

    public RacingPlugin() {
        instance = this;
    }

    public static RacingPlugin getInstance() {
        if (instance == null) {
            instance = new RacingPlugin();
        }
        return instance;
    }

    public static void setRacing(Racing racing) {
        RacingPlugin.racing = racing;
    }

    public static Racing getRacing() {
        return racing;
    }

    public void onLoad() {
        Thread.currentThread().setContextClassLoader(this.getClassLoader());

        // Vérifie les fichiers de configuration du plugin
        if (new File("./plugins/racing").mkdir()) {
            getLogger().log(Level.INFO, "Création du dossier de configuration du plugin");
        }

        if (new File("./plugins/racing/tracks").mkdir()) {
            getLogger().log(Level.INFO, "Création du dossier de courses");
        }

        loadTracks();
    }

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("game")).setExecutor(new CommandCheckpoint());
        Objects.requireNonNull(getCommand("track")).setExecutor(new CommandTrack());
    }

    @Override
    public void onDisable() {
        saveTracks();
    }

    public void loadTracks() {
        File tracksDir = new File("./plugins/racing/tracks/");
        File[] files = tracksDir.listFiles();

        if (files != null) {
            for (File trackFile : files) {
                try {
                    InputStream is = new FileInputStream(trackFile);
                    Yaml trackYamlFile = new Yaml(new Constructor(Track.class));

                    Track track = trackYamlFile.loadAs(is, net.ddns.lagarderie.cubiplugin.game.Track.class);

                    this.tracks.add(track);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            getLogger().log(Level.WARNING, "Aucun fichier de courses dans le dossier");
        }
    }

    public void saveTracks() {
        File tracksDir = new File("./plugins/racing/tracks/");
        File[] files = tracksDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.delete()) {
                    getLogger().log(Level.INFO, "Les fichiers de courses ont été écrasés");
                }
            }

            for (Track track : this.tracks) {
                try {
                    Yaml yaml = new Yaml();
                    String fileName = tracksDir.getPath() + "\\" + track.getMapId() + ".yml";

                    FileWriter writer = new FileWriter(fileName);
                    yaml.dump(track, writer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public TrackDebugMode getTrackDebugMode() {
        return trackDebugMode;
    }
}
