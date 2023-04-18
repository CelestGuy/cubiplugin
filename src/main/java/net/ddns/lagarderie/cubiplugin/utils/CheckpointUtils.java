package net.ddns.lagarderie.cubiplugin.utils;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import net.ddns.lagarderie.cubiplugin.game.Checkpoint;
import net.ddns.lagarderie.cubiplugin.game.Track;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CheckpointUtils {
    public static Checkpoint getClosestCheckpoint(Player player, Track track) throws RacingGameException {
        if (track.getCheckpoints() != null && player != null) {
            Vector playerLocation = player.getLocation().toVector();

            Checkpoint closestCheckpoint = null;

            for (Checkpoint checkpoint : track.getCheckpoints()) {
                Vector checkpointLocation = checkpoint.getTrackLocation().toVector();

                if (closestCheckpoint == null ||
                        playerLocation.clone().subtract(closestCheckpoint.getTrackLocation().toVector()).lengthSquared() >
                        playerLocation.clone().subtract(checkpointLocation).lengthSquared()) {
                    closestCheckpoint = checkpoint;
                }
            }

            return closestCheckpoint;
        } else {
            throw new RacingGameException("Les paramètres ne peuvent être null");
        }
    }
}
