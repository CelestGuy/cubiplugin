package net.ddns.lagarderie.racingplugin.utils;

import com.google.gson.Gson;
import net.ddns.lagarderie.racingplugin.RacingPlugin;
import net.ddns.lagarderie.racingplugin.game.PowerBlock;
import net.ddns.lagarderie.racingplugin.plugin.RacingGameException;
import net.ddns.lagarderie.racingplugin.game.Checkpoint;
import net.ddns.lagarderie.racingplugin.game.Track;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.*;
import java.util.*;

public class TrackUtils {
    public static Track getTrack(String trackId) throws RacingGameException {
        for (Track track : RacingPlugin.getPlugin().getTracks()) {
            if (track.getId().equals(trackId)) {
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

    public static float getShortestDistance(Track track, Checkpoint a, Checkpoint b) {
        // TODO : Enhance this cringe code

        int aId = a.getId();
        int bId = b.getId();

        HashMap<Integer, Float> distances = new HashMap<>();
        HashSet<Integer> marks = new HashSet<>();
        LinkedList<Integer> file = new LinkedList<>();

        for (int i = 0; i < track.getCheckpoints().size(); i++) {
            distances.put(i, Float.MAX_VALUE);
        }

        file.add(aId);
        distances.replace(aId, 0f);

        while (!file.isEmpty()) {
            double min = Double.POSITIVE_INFINITY;
            int index = 0;
            for (int i = 0; i < file.size(); i++) {
                if (distances.get(file.get(i)) < min) {
                    min = distances.get(file.get(i));
                    index = i;
                }
            }
            int cId = file.remove(index);
            marks.add(cId);

            Checkpoint c = track.getCheckpoint(cId);

            for (Integer childId : c.getChildren()) {
                Checkpoint child = track.getCheckpoint(childId);
                float distance = distances.get(cId) + getDistance(c, child);

                if (childId == bId) {
                    distances.put(childId, distance);
                    return distances.get(childId);
                } else if (!file.contains(childId) && !marks.contains(childId)) {
                    distances.put(childId, distance);
                    file.add(childId);
                } else if (distance < distances.get(childId)) {
                    distances.replace(childId, distance);
                }
            }
        }

        return distances.get(bId);
    }
}
