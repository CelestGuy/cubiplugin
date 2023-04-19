package net.ddns.lagarderie.cubiplugin.modes;

import net.ddns.lagarderie.cubiplugin.RacingPlugin;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import net.ddns.lagarderie.cubiplugin.game.Checkpoint;
import net.ddns.lagarderie.cubiplugin.game.Track;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import static net.ddns.lagarderie.cubiplugin.utils.CheckpointUtils.*;
import static net.ddns.lagarderie.cubiplugin.utils.TrackUtils.getTrack;

public class CheckpointAutoPlaceMode implements RacingMode {
    private boolean running = false;
    private final String trackId;
    private final Player player;

    private int defaultCheckpointRadius;

    public CheckpointAutoPlaceMode(Player player, Track track) {
        this.player = player;
        this.trackId = track.getMapId();
        this.defaultCheckpointRadius = 1;
    }

    public void start() {
        running = true;
        run();
    }

    public void run() {
        BukkitScheduler scheduler = Bukkit.getScheduler();

        scheduler.runTaskTimer(RacingPlugin.getInstance(), task -> {
            if (!running) {
                task.cancel();
            } else {
                Track track;
                Checkpoint closestCheckpoint;

                try {
                    track = getTrack(trackId);
                    closestCheckpoint = getClosestCheckpoint(player, track);
                } catch (RacingGameException e) {
                    throw new RuntimeException(e);
                }

                if (closestCheckpoint == null || !isPlayerInCheckpoint(player, closestCheckpoint)) {
                    Checkpoint newCheckpoint = new Checkpoint();
                    newCheckpoint.setTrackLocation(getNewLocation(player.getLocation()));
                    newCheckpoint.setRadius(defaultCheckpointRadius);

                    if (closestCheckpoint != null) {
                        closestCheckpoint.addChildCheckpoint(track.getCheckpoints().size());
                    }

                    track.addCheckpoint(newCheckpoint);
                }
            }
        }, 0L, 5L);
    }

    public void stop() {
        running = false;
    }

    public int getDefaultCheckpointRadius() {
        return this.defaultCheckpointRadius;
    }

    public void setDefaultCheckpointRadius(int value) {
        this.defaultCheckpointRadius = value;
    }
}
