package net.ddns.lagarderie.cubiplugin.utils;

import net.ddns.lagarderie.cubiplugin.game.Checkpoint;
import net.ddns.lagarderie.cubiplugin.game.Track;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class CheckpointUtils {
    public static Checkpoint getCheckpoint(Track track, int checkpointId) {
        if (checkpointId >= 0 && checkpointId < track.getCheckpoints().size()) {
            return track.getCheckpoints().get(checkpointId);
        }

        return null;
    }

    public static Checkpoint getClosestCheckpoint(Player player, Track track) {
        if (track.getCheckpoints().size() > 0 && player != null) {
            List<Checkpoint> checkpoints = track.getCheckpoints();

            Checkpoint closestCheckpoint = checkpoints.get(0);
            Vector closestCheckpointLocation = closestCheckpoint.getLocation().clone().toVector();

            for (Checkpoint checkpoint : checkpoints) {
                Vector checkpointLocation = checkpoint.getLocation().clone().toVector();
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

    public static boolean isPlayerInCheckpoint(Player player, Checkpoint checkpoint) {
        Vector playerLocation = player.getLocation().toVector();

        Vector checkpointLocation = checkpoint.getLocation().toVector();

        return playerLocation.subtract(checkpointLocation).lengthSquared() <=
                (checkpoint.getRadius() * checkpoint.getRadius());
    }
}
