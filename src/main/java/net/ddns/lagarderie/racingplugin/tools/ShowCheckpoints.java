package net.ddns.lagarderie.racingplugin.tools;

import net.ddns.lagarderie.racingplugin.RacingPlugin;
import net.ddns.lagarderie.racingplugin.game.Checkpoint;
import net.ddns.lagarderie.racingplugin.game.Track;
import net.ddns.lagarderie.racingplugin.plugin.RacingGameException;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.UUID;

import static net.ddns.lagarderie.racingplugin.utils.ParticleUtils.drawCircle;
import static net.ddns.lagarderie.racingplugin.utils.ParticleUtils.spawnRedstoneParticle;
import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.*;
import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.Bukkit.getServer;

public class ShowCheckpoints {
    private static final HashMap<UUID, ShowCheckpoints> players = new HashMap<>();
    private final UUID playerUuid;
    private boolean enabled = false;


    public ShowCheckpoints(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    public static void removePlayer(UUID uuid) {
        players.get(uuid).disable();
        players.remove(uuid);
    }

    public static ShowCheckpoints getPlayer(UUID uuid) {
        if (players.get(uuid) == null) {
            players.put(uuid, new ShowCheckpoints(uuid));
        }

        return players.get(uuid);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void enable() {
        enabled = true;
        loop();
    }

    public void disable() {
        enabled = false;
    }

    public void loop() {
        BukkitScheduler scheduler = getScheduler();

        scheduler.runTaskTimer(RacingPlugin.getRacingPlugin(), task -> {
            if (enabled) {
                run();
            } else {
                task.cancel();
            }
        }, 0L, 5L);
    }

    public void run() {
        Player player = getServer().getPlayer(playerUuid);
        Track track;

        if (player != null) {
            try {
                track = getTrack(player.getWorld().getName());
            } catch (RacingGameException e) {
                disable();
                throw new RuntimeException(e);
            }

            World world = player.getWorld();

            if (world.getName().equals(track.getMapId())) {
                Checkpoint playersClosestCheckpoint;
                playersClosestCheckpoint = getClosestCheckpoint(player, track);

                for (Checkpoint checkpoint : getCheckpointsNearPlayer(player, track, 16)) {
                    Color color = Color.AQUA;

                    if (checkpoint.getId() == track.getDepartureCheckpoint()) {
                        color = Color.GREEN;
                    } else if (checkpoint.getId() == track.getArrivalCheckpoint()) {
                        color = Color.RED;
                    }

                    if (checkpoint == playersClosestCheckpoint) {
                        color = Color.YELLOW;
                    }

                    spawnRedstoneParticle(player, checkpoint.getPosition(), color, 0.5f);
                    drawCircle(player, checkpoint.getPosition(), Color.TEAL, checkpoint.getRadius());
                }
            }
        }
    }
}
