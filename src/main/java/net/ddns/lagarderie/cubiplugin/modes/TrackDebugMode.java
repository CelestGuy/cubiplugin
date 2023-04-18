package net.ddns.lagarderie.cubiplugin.modes;

import net.ddns.lagarderie.cubiplugin.RacingPlugin;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import net.ddns.lagarderie.cubiplugin.game.Checkpoint;
import net.ddns.lagarderie.cubiplugin.game.Racing;
import net.ddns.lagarderie.cubiplugin.game.Track;
import net.ddns.lagarderie.cubiplugin.game.TrackLocation;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;

import static net.ddns.lagarderie.cubiplugin.utils.CheckpointUtils.getClosestCheckpoint;
import static net.ddns.lagarderie.cubiplugin.utils.TrackUtils.getTrack;
import static org.bukkit.Bukkit.getServer;

public class TrackDebugMode implements RacingMode {
    private final boolean[] running = {false};
    private final String trackId;
    private final Player player;


    public TrackDebugMode(Player player, Track track) {
        this.player = player;
        this.trackId = track.getMapId();
    }

    public void start() {
        running[0] = true;
        run();
    }

    public void run() {
        BukkitScheduler scheduler = Bukkit.getScheduler();

        scheduler.runTaskTimer(RacingPlugin.getInstance(), task -> {
            if (!running[0]) {
                task.cancel();
            } else {
                Track track;

                try {
                    track = getTrack(trackId);
                } catch (RacingGameException e) {
                    throw new RuntimeException(e);
                }

                TrackLocation departure = track.getDeparture();
                World world = player.getWorld();

                if (world.getName().equals(track.getMapId())) {
                    Checkpoint playersClosestCheckpoint = null;
                    try {
                        playersClosestCheckpoint = getClosestCheckpoint(player, track);
                    } catch (RacingGameException ignored) {}

                    for (Checkpoint checkpoint : track.getCheckpoints()) {
                        Color color = Color.AQUA;
                        if (checkpoint == playersClosestCheckpoint) {
                            color = Color.YELLOW;
                        }

                        spawnRedstoneParticle(checkpoint.getTrackLocation(), color, 0.5f);
                        drawCircle(checkpoint.getTrackLocation(), checkpoint.getRadius());
                    }

                    if (departure != null) {
                        spawnRedstoneParticle(departure, Color.RED, 0.5f);
                    }
                }
            }
        }, 0L, 5L);
    }

    public void stop() {
        running[0] = false;
    }

    private void drawCircle(Location location, float radius) {
        float dx = 0;
        float dy = (radius * 2f);
        float d = 3f - (2f * (radius * 2f));

        do {
            drawCircle(location, dx, dy);
            drawCircle(location, dy, dx);

            dx++;

            if (d > 0) {
                dy--;
                d += 4f * (dx - dy) + 10f;
            } else {
                d += 4f * dx + 6f;
            }
        } while (dy >= dx);
    }

    private void drawCircle(Location location, float dx, float dy) {
        float ddx = dx * .5f;
        float ddy = dy * .5f;

        spawnRedstoneParticle(location.clone().add(ddx, 0, ddy), Color.TEAL, 0.1);
        spawnRedstoneParticle(location.clone().add(-ddx, 0, ddy), Color.TEAL, 0.1);
        spawnRedstoneParticle(location.clone().add(ddx, 0, -ddy), Color.TEAL, 0.1);
        spawnRedstoneParticle(location.clone().add(-ddx, 0, -ddy), Color.TEAL, 0.1);
    }

    private void spawnRedstoneParticle(Location location, Color color, double deltaY) {
        DustOptions options = new DustOptions(color, 1f);
        player.spawnParticle(
                Particle.REDSTONE,
                location.getX(),
                location.getY(),
                location.getZ(),
                5,
                0,
                deltaY,
                0,
                options
        );
    }
}
