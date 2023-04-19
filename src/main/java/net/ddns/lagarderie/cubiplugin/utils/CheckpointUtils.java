package net.ddns.lagarderie.cubiplugin.utils;

import net.ddns.lagarderie.cubiplugin.game.Checkpoint;
import net.ddns.lagarderie.cubiplugin.game.Track;
import net.ddns.lagarderie.cubiplugin.game.TrackLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class CheckpointUtils {
    public static Checkpoint getCheckpoint(int index, Track track) {
        if (index >= 0 && index < track.getCheckpoints().size()) {
            return track.getCheckpoints().get(index);
        }

        return null;
    }

    public static Checkpoint getClosestCheckpoint(Player player, Track track) {
        if (track.getCheckpoints().size() > 0 && player != null) {
            List<Checkpoint> checkpoints = track.getCheckpoints();

            Checkpoint closestCheckpoint = checkpoints.get(0);
            Vector closestCheckpointLocation = closestCheckpoint.getTrackLocation().clone().toVector();

            for (Checkpoint checkpoint : checkpoints) {
                Vector checkpointLocation = checkpoint.getTrackLocation().clone().toVector();
                Vector playerLocation = player.getLocation().clone().toVector();

                double playerToClosestCheckpointSquaredLength = playerLocation.clone().subtract(closestCheckpointLocation).lengthSquared();
                double playerToCheckpointSquaredLength = playerLocation.clone().subtract(checkpointLocation).lengthSquared();

                if (playerToClosestCheckpointSquaredLength > playerToCheckpointSquaredLength) {
                    closestCheckpoint = checkpoint;
                    closestCheckpointLocation = checkpointLocation;
                }
            }

            return closestCheckpoint;
        }

        return null;
    }

    public static TrackLocation getNewLocation(Location currentLocation) {
        currentLocation.setX((int) (currentLocation.getX()) + (0.5 * (currentLocation.getX() < 0 ? -1 : 1)));
        currentLocation.setY((int) (currentLocation.getY()));
        currentLocation.setZ((int) (currentLocation.getZ()) + (0.5 * (currentLocation.getZ() < 0 ? -1 : 1)));

        return new TrackLocation(currentLocation.clone());
    }

    public static boolean isPlayerInCheckpoint(Player player, Checkpoint checkpoint) {
        Vector playerLocation = player.getLocation().toVector();

        Vector checkpointLocation = checkpoint.getTrackLocation().toVector();

        return playerLocation.subtract(checkpointLocation).lengthSquared() <=
                (checkpoint.getRadius() * checkpoint.getRadius());
    }
}
