package net.ddns.lagarderie.cubiplugin.utils;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import net.ddns.lagarderie.cubiplugin.game.Checkpoint;
import net.ddns.lagarderie.cubiplugin.game.Racing;
import net.ddns.lagarderie.cubiplugin.game.Track;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TrackUtils {
    public static Track getTrack(String trackId) throws RacingGameException {
        for (Track track : Racing.tracks) {
            if (track.getMapId().equals(trackId)) {
                return track;
            }
        }

        throw new RacingGameException("Cet id de course n'existe pas.");
    }

    public static List<Checkpoint> getCheckpointsNearPlayer(Player player, Track track, int maxDistance) {
        ArrayList<Checkpoint> checkpoints = new ArrayList<>();

        Vector playerLocation = player.getLocation().toVector();
        int maxDistanceSquared = maxDistance * maxDistance;


        for (Checkpoint checkpoint : track.getCheckpoints()) {
            Vector checkpointLocation = checkpoint.getLocation().clone().toVector();

            if (playerLocation.clone().subtract(checkpointLocation).lengthSquared() <= maxDistanceSquared) {
                checkpoints.add(checkpoint);
            }
        }

        return checkpoints;
    }

    public static ArrayList<Track> loadTracks() {
        ArrayList<Track> tracks = new ArrayList<>();

        File tracksDir = new File("./plugins/racing/tracks/");
        File[] files = tracksDir.listFiles();

        if (files != null) {
            for (File trackFile : files) {
                try {
                    InputStream is = new FileInputStream(trackFile);
                    Yaml trackYamlFile = new Yaml(new Constructor(Track.class));

                    Track track = trackYamlFile.loadAs(is, net.ddns.lagarderie.cubiplugin.game.Track.class);

                    tracks.add(track);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            System.err.println("Aucun fichier de courses dans le dossier");
        }

        return tracks;
    }

    public static void saveTrack(Track track) {
        File trackFile = new File("./plugins/racing/tracks/" + track.getMapId() + ".yml");

        if (trackFile.exists()) {
            if (trackFile.delete()) {
                System.out.println("Les fichier de course a été écrasé");
            } else {
                System.err.println("Impossible de supprimer le fichier de course");
            }
        }

        try {
            Yaml yaml = new Yaml();
            FileWriter writer = new FileWriter(trackFile.getPath());

            yaml.dump(track, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveTracks(ArrayList<Track> tracks) {
        File tracksDir = new File("./plugins/racing/tracks/");
        File[] files = tracksDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.delete()) {
                    System.out.println("Les fichiers de courses ont été écrasés");
                }
            }

            for (Track track : tracks) {
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
}
