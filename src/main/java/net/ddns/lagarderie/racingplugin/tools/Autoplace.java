package net.ddns.lagarderie.racingplugin.tools;

import net.ddns.lagarderie.racingplugin.RacingPlugin;
import net.ddns.lagarderie.racingplugin.game.Checkpoint;
import net.ddns.lagarderie.racingplugin.game.Track;
import net.ddns.lagarderie.racingplugin.plugin.RacingCommandException;
import net.ddns.lagarderie.racingplugin.plugin.RacingGameException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.UUID;

import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.*;
import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.Bukkit.getServer;

public class Autoplace {
    private static final HashMap<UUID, Autoplace> players = new HashMap<>();

    private final UUID playerUuid;
    private boolean enabled = false;

    private int defaultCheckpointRadius;
    private int startId;
    private int endId;

    private Checkpoint lastPlacedCheckpoint;

    public Autoplace(UUID playerUuid) {
        this.playerUuid = playerUuid;
        this.defaultCheckpointRadius = 1;
        this.startId = 0;
        this.endId = 0;
    }

    public static void removePlayer(UUID uuid) {
        players.get(uuid).disable();
        players.remove(uuid);
    }

    public static Autoplace getPlayer(UUID uuid) {
        if (players.get(uuid) == null) {
            players.put(uuid, new Autoplace(uuid));
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
        Track track;
        Player player = getServer().getPlayer(playerUuid);

        if (player != null) {
            try {
                track = getTrack(player.getWorld().getName());
            } catch (RacingGameException e) {
                throw new RuntimeException(e);
            }

            Checkpoint departureCheckpoint = track.getCheckpoint(startId);
            Checkpoint arrivalCheckpoint = track.getCheckpoint(endId);

            int maxId = getMaxCheckpointId(track);

            if (lastPlacedCheckpoint == null || !isPlayerInCheckpoint(player, lastPlacedCheckpoint)) {
                Location playerLocation = player.getLocation();

                Checkpoint newCheckpoint = new Checkpoint(maxId + 1, playerLocation.toVector(), playerLocation.getYaw(), playerLocation.getPitch(), defaultCheckpointRadius);

                if (lastPlacedCheckpoint != null) {
                    try {
                        lastPlacedCheckpoint.addChildCheckpoint(track.getCheckpoints().size());
                    } catch (RacingGameException e) {
                        throw new RacingCommandException(e.getMessage());
                    }
                }

                track.addCheckpoint(newCheckpoint);
                lastPlacedCheckpoint = newCheckpoint;
            } else if (isPlayerInCheckpoint(player, arrivalCheckpoint) && lastPlacedCheckpoint != arrivalCheckpoint) {
                try {
                    lastPlacedCheckpoint.addChildCheckpoint(endId);
                    player.sendMessage(ChatColor.GREEN + "Checkpoint d'arrivée atteint, désactivation du placement automatique." + ChatColor.RESET);
                } catch (RacingGameException e) {
                    player.sendMessage(ChatColor.RED + e.getMessage() + ChatColor.RESET);
                }
                disable();
            }
        } else {
            removePlayer(playerUuid);
        }
    }

    public int getDefaultCheckpointRadius() {
        return this.defaultCheckpointRadius;
    }

    public void setDefaultCheckpointRadius(int value) {
        this.defaultCheckpointRadius = value;
    }

    public int getStartId() {
        return startId;
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }

    public int getEndId() {
        return endId;
    }

    public void setEndId(int endId) {
        this.endId = endId;
    }
}
