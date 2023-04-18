package net.ddns.lagarderie.cubiplugin.modes;

import net.ddns.lagarderie.cubiplugin.RacingPlugin;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import net.ddns.lagarderie.cubiplugin.game.Checkpoint;
import net.ddns.lagarderie.cubiplugin.game.Track;
import net.ddns.lagarderie.cubiplugin.game.TrackLocation;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import static net.ddns.lagarderie.cubiplugin.utils.CheckpointUtils.getClosestCheckpoint;
import static net.ddns.lagarderie.cubiplugin.utils.TrackUtils.getTrack;

public class CheckpointDrawingMode implements RacingMode {
    private boolean running = false;
    private final String trackId;
    private final Player player;

    private int defaultCheckpointRadius;

    public CheckpointDrawingMode(Player player, Track track) {
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
