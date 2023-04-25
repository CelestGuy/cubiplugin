package net.ddns.lagarderie.cubiplugin.game;

import net.ddns.lagarderie.cubiplugin.RacingPlugin;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.util.*;

import static net.ddns.lagarderie.cubiplugin.utils.TrackGraph.getShortestDistance;
import static net.ddns.lagarderie.cubiplugin.utils.CheckpointUtils.getClosestCheckpoint;
import static net.ddns.lagarderie.cubiplugin.utils.TrackUtils.loadTracks;
import static org.bukkit.Bukkit.getServer;

public class Racing {
    public static final ArrayList<Track> tracks = loadTracks();
    private static Racing instance = null;

    private LinkedList<RacingPlayer> racingPlayers;
    private Track track;
    private int speed;
    private int lapCount;
    private boolean running;

    private Racing() {
        this.racingPlayers = new LinkedList<>();
        this.track = null;
        this.speed = 100;
        this.lapCount = 3;
        this.running = false;
    }

    public static Racing getInstance() {
        if (instance == null) {
            instance = new Racing();
        }
        return instance;
    }

    public void start() throws RacingGameException {
        this.running = true;
        this.racingPlayers = new LinkedList<>();

        World world = getServer().getWorld(track.getMapId());

        BukkitScheduler scheduler = Bukkit.getScheduler();

        final int[] timer = {3};
        final int[] counter = {0};

        for (Player player : getServer().getOnlinePlayers()) {
            racingPlayers.add(new RacingPlayer(player.getUniqueId()));
        }

        scheduler.runTaskTimer(RacingPlugin.getInstance(), counterTask -> {
            if (!running || timer[0] == 0) {
                counterTask.cancel();
                run();
            } else {
                Checkpoint firstCheckpoint;

                if (track.getCheckpoints().size() > 0) {
                    firstCheckpoint = track.getCheckpoints().get(0);

                    for (RacingPlayer racingPlayer : racingPlayers) {
                        UUID playerUuid = racingPlayer.getUuid();
                        Player player = getServer().getPlayer(playerUuid);
                        Location location = firstCheckpoint.getLocation().clone();
                        location.setWorld(world);

                        if (player != null) {
                            player.teleport(location);
                            player.sendTitle("" + timer[0], "", 0, 20, 0);
                        }
                    }
                } else {
                    stop();
                }

                counter[0] += 5;
                running = !racingPlayers.isEmpty();

                if (counter[0] % 20 == 0) {
                    timer[0]--;
                }
            }
        }, 0L, 5L);
    }

    public void run() {
        World world = getServer().getWorld(track.getMapId());

        BukkitScheduler scheduler = Bukkit.getScheduler();

        final int[] counter = {0};

        for (RacingPlayer racingPlayer : racingPlayers) {
            racingPlayer.setCheckedLaps(0);
            racingPlayer.setCheckedCheckpoints(0);
        }

        float trackLength = getShortestDistance(track, track.getCheckpoint(track.getDepartureCheckpoint()), track.getCheckpoint(track.getArrivalCheckpoint()));

        scheduler.runTaskTimer(RacingPlugin.getInstance(), gameTask -> {
            if (!running || world == null) {
                gameTask.cancel();
            } else {
                sortPlayers(trackLength);
                RacingPlayer[] rps = new RacingPlayer[racingPlayers.size()];
                racingPlayers.toArray(rps);

                for (int i = 0; i < rps.length; i++) {
                    RacingPlayer racingPlayer = rps[i];
                    Player player = getServer().getPlayer(racingPlayer.getUuid());

                    if (player != null) {
                        Block block = world.getBlockAt(player.getLocation().subtract(new Vector(0, 1, 0)));

                        int speedEffect = Math.min(speed / 10, 255);

                        switch (block.getType()) {
                            case YELLOW_CONCRETE, YELLOW_TERRACOTTA -> {
                                int boostedSpeedEffect = Math.min((speedEffect + 5), 255);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10, boostedSpeedEffect, false));
                            }
                            case GREEN_CONCRETE -> player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5, 5, false));
                            case MAGENTA_CONCRETE -> player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 5, 15, false));
                            default -> player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1, speedEffect, false));
                        }

                        String playerTime = String.format(Locale.US, "%.2f", (counter[0] / 20f));

                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                                (i + 1) + "e - " + racingPlayer.getDistance() + " - Temps : " + playerTime + " s"
                        ));

                        // validate checkpoints

                        Checkpoint nearestCheckpoint = getClosestCheckpoint(player, track);
                        Checkpoint validatedCheckpoint = track.getCheckpoint(racingPlayer.getCheckedCheckpoints());

                        if (nearestCheckpoint != null) {
                            for (Integer childId : validatedCheckpoint.getChildren()) {
                                if (nearestCheckpoint.getId() == childId) {
                                    racingPlayer.setCheckedCheckpoints(childId);
                                    if (childId == track.getDepartureCheckpoint()) {
                                        int lap = racingPlayer.getCheckedLaps() + 1;
                                        racingPlayer.setCheckedLaps(lap);
                                        player.sendTitle((lap + 1) + "e tour", "", 10, 40, 10);
                                    }
                                    break;
                                }
                            }
                        }
                    } else {
                        racingPlayers.remove(racingPlayer);
                    }
                }

                counter[0]++;
                running = !havePlayersFinished();
            }
        }, 0L, 1L);
    }

    private void sortPlayers(float trackLength) {
        Checkpoint arrival = track.getCheckpoint(track.getArrivalCheckpoint());

        for (RacingPlayer racingPlayer : racingPlayers) {
            UUID playerUuid = racingPlayer.getUuid();

            Player player = getServer().getPlayer(playerUuid);
            Checkpoint nearestCheckpoint = getClosestCheckpoint(player, track);

            if (nearestCheckpoint != null && player != null) {
                float d = getShortestDistance(track, nearestCheckpoint, arrival);

                Vector playerPos = player.getLocation().toVector();
                Vector checkpoint = nearestCheckpoint.getLocation().toVector();

                d += playerPos.subtract(checkpoint).length() + (lapCount - (racingPlayer.getCheckedLaps())) * trackLength;

                racingPlayer.setDistance(d);
            }
        }

        Collections.sort(racingPlayers);
    }

    private boolean havePlayersFinished() {
        for (RacingPlayer racingPlayer : racingPlayers) {
            if (racingPlayer.getCheckedLaps() < lapCount) {
                return false;
            }
        }

        return true;
    }

    public void stop() {
        running = false;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return track;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getLapCount() {
        return lapCount;
    }

    public void setLapCount(int lapCount) {
        this.lapCount = lapCount;
    }

    public boolean isRunning() {
        return running;
    }
}
