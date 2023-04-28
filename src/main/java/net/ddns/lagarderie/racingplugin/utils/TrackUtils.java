package net.ddns.lagarderie.racingplugin.utils;

import com.google.gson.Gson;
import net.ddns.lagarderie.racingplugin.RacingPlugin;
import net.ddns.lagarderie.racingplugin.plugin.RacingGameException;
import net.ddns.lagarderie.racingplugin.game.Checkpoint;
import net.ddns.lagarderie.racingplugin.game.Track;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TrackUtils {
    public static Track getTrack(String trackId) throws RacingGameException {
        for (Track track : RacingPlugin.getRacingPlugin().getTracks()) {
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
            Vector checkpointLocation = checkpoint.getPosition();

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
                    FileReader reader = new FileReader(trackFile);

                    Gson gson = new Gson();
                    Track track = gson.fromJson(reader, Track.class);
                    tracks.add(track);

                    reader.close();
                } catch (IOException e) {
                    System.err.println("Impossible de charger la course " + trackFile.getName() + ": " + e);
                }
            }
        } else {
            System.err.println("Aucun fichier de courses dans le dossier");
        }

        return tracks;
    }

    public static void saveTrack(Track track) {
        File trackFile = new File("./plugins/racing/tracks/" + track.getMapId() + ".json");

        if (trackFile.exists()) {
            if (trackFile.delete()) {
                System.out.println("Les fichier de course a été écrasé");
            } else {
                System.err.println("Impossible de supprimer le fichier de course");
            }
        }

        try {
            Gson jo = new Gson();
            String json = jo.toJson(track);
            FileWriter file = new FileWriter(trackFile.getPath());

            file.write(json);
            file.flush();
            file.close();
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
                saveTrack(track);
            }
        }
    }

    public static float getDistance(Checkpoint a, Checkpoint b) {
        Vector vecA = a.getPosition().clone();
        Vector vecB = b.getPosition().clone();

        return getDistance(vecA, vecB);
    }

    public static float getDistance(Vector a, Vector b) {
        return (float) a.clone().subtract(b.clone()).length();
    }

    public static Checkpoint getClosestCheckpoint(Player player, Track track) {
        if (track.getCheckpoints().size() > 0 && player != null) {
            List<Checkpoint> checkpoints = track.getCheckpoints();

            Checkpoint closestCheckpoint = checkpoints.get(0);

            for (Checkpoint checkpoint : checkpoints) {
                Vector playerPosition = player.getLocation().toVector();
                Vector checkpointLocation = checkpoint.getPosition().clone();
                Vector closestCheckpointLocation = closestCheckpoint.getPosition().clone();

                double playerToClosestCheckpointSquaredLength = closestCheckpointLocation.subtract(playerPosition).lengthSquared();
                double playerToCheckpointSquaredLength = checkpointLocation.subtract(playerPosition).lengthSquared();

                if (playerToClosestCheckpointSquaredLength > playerToCheckpointSquaredLength) {
                    closestCheckpoint = checkpoint;
                }
            }

            return closestCheckpoint;
        }

        return null;
    }

    public static boolean isPlayerInCheckpoint(Player player, Checkpoint checkpoint) {
        if (player != null && checkpoint != null) {
            Vector playerLocation = player.getLocation().toVector();
            Vector checkpointLocation = checkpoint.getPosition();

            return playerLocation.subtract(checkpointLocation).lengthSquared() <=
                    (checkpoint.getRadius() * checkpoint.getRadius());
        }

        return false;
    }

    public static Vector getCenteredPosition(Vector position) {
        if (position.getX() < 0) {
            position.setX((int) (position.getX()) - 0.5);
        } else {
            position.setX((int) (position.getX()) + 0.5);
        }

        position.setY((int) (position.getY()));

        if (position.getZ() < 0) {
            position.setZ((int) (position.getZ()) - 0.5);
        } else {
            position.setZ((int) (position.getZ()) + 0.5);
        }

        return position;
    }

    public static int getMaxCheckpointId(Track track) {
        int maxId = -1;
        for (Checkpoint c : track.getCheckpoints()) {
            if (c.getId() > maxId) {
                maxId = c.getId();
            }
        }
        return maxId;
    }

    public static Location getCheckpointLocation(World world, Checkpoint checkpoint) {
        Vector position = checkpoint.getPosition();

        return new Location(world, position.getX(), position.getY(), position.getZ(), checkpoint.getYaw(), checkpoint.getPitch());
    }
}
