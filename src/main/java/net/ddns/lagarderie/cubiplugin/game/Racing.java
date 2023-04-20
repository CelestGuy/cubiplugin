package net.ddns.lagarderie.cubiplugin.game;

import net.ddns.lagarderie.cubiplugin.RacingPlugin;
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
    private static Racing instance = null;
    public static final ArrayList<Track> tracks = loadTracks();

    private final HashSet<UUID> players;
    private Track track = null;
    private int speed = 150;
    private int lapCount = 3;
    private boolean running = false;

    private Racing() {
        players = new HashSet<>();
    }

    public static Racing getInstance() {
        if (instance == null) {
            instance = new Racing();
        }
        return instance;
    }

    public void start() {
        running = true;
        World world = getServer().getWorld(track.getMapId());

        BukkitScheduler scheduler = Bukkit.getScheduler();

        final int[] timer = {3};
        final int[] counter = {0};

        for (Player player : getServer().getOnlinePlayers()) {
            players.add(player.getUniqueId());
        }

        scheduler.runTaskTimer(RacingPlugin.getInstance(), counterTask -> {
            if (!running || timer[0] == 0) {
                counterTask.cancel();
                run();
            } else {
                Checkpoint firstCheckpoint;

                if (track.getCheckpoints().size() > 0) {
                    firstCheckpoint = track.getCheckpoints().get(0);

                    for (UUID playerUuid : players) {
                        Player player = getServer().getPlayer(playerUuid);
                        Location location = firstCheckpoint.getTrackLocation().clone();
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
                running = !players.isEmpty();

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

        HashMap<UUID, Integer> playersPoints = new HashMap<>();
        HashMap<UUID, Integer> playersCheckedCheckpoints = new HashMap<>();

        for (UUID playerUuid : players) {
            playersPoints.put(playerUuid, 0);
            playersCheckedCheckpoints.put(playerUuid, 0);
        }

        float trackLength = getShortestDistance(track, track.getCheckpoint(track.getDepartureCheckpoint()), track.getCheckpoint(track.getArrivalCheckpoint()));

        scheduler.runTaskTimer(RacingPlugin.getInstance(), gameTask -> {
            if (!running || world == null) {
                gameTask.cancel();
            } else {
                ArrayList<PlayerDistance> playerDistances = getPlayersPositions(trackLength, playersPoints);

                for (int i = 0; i < playerDistances.size(); i++) {
                    PlayerDistance playerDistance = playerDistances.get(i);
                    Player player = getServer().getPlayer(playerDistance.getUuid());

                    if (player != null) {
                        Block block = world.getBlockAt(player.getLocation().subtract(new Vector(0, 1, 0)));

                        int speedEffect = Math.min(speed / 10, 255);

                        switch (block.getType()) {
                            case YELLOW_CONCRETE, YELLOW_TERRACOTTA -> {
                                int boostedSpeedEffect = Math.min((speedEffect + 5), 255);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10, boostedSpeedEffect, false));
                            }
                            case GREEN_CONCRETE -> {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5, 5, false));
                            }
                            case MAGENTA_CONCRETE -> {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 5, 15, false));
                            }
                            default -> {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1, speedEffect, false));
                            }
                        }

                        String playerTime = String.format(Locale.US, "%.2f", (counter[0] / 20f));

                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                                (i + 1) + "e - " + playerDistance.getDistance() + " - Temps : " + playerTime + " s"
                        ));

                        // validate checkpoints

                        Checkpoint nearestCheckpoint = getClosestCheckpoint(player, track);
                        Checkpoint validatedCheckpoint = track.getCheckpoint(playersCheckedCheckpoints.get(playerDistance.getUuid()));

                        if (nearestCheckpoint != null) {
                            for (Integer childId : validatedCheckpoint.getChildren()) {
                                if (nearestCheckpoint.getId() == childId) {
                                    playersCheckedCheckpoints.put(playerDistance.getUuid(), childId);
                                    if (childId == track.getDepartureCheckpoint()) {
                                        int lap = playersPoints.get(playerDistance.getUuid()) + 1;
                                        playersPoints.put(playerDistance.getUuid(), lap);
                                        player.sendTitle((lap + 1) + "e tour", "", 10, 40, 10);
                                    }
                                    player.sendMessage(childId + " Validé");
                                    break;
                                }
                            }
                        }
                    } else {
                        players.remove(playerDistance.getUuid());
                    }
                }

                counter[0]++;
                running = !havePlayersFinished(playersPoints);
            }
        }, 0L, 1L);
    }

    private ArrayList<PlayerDistance> getPlayersPositions(float trackLength, HashMap<UUID, Integer> playersPoints) {
        ArrayList<PlayerDistance> playerDistances = new ArrayList<>();

        Checkpoint arrival = track.getCheckpoint(track.getArrivalCheckpoint());

        for (UUID playerUuid : players) {
            Player player = getServer().getPlayer(playerUuid);
            Checkpoint nearestCheckpoint = getClosestCheckpoint(player, track);

            if (nearestCheckpoint != null && player != null) {
                float d = getShortestDistance(track, nearestCheckpoint, arrival);

                Vector playerPos = player.getLocation().toVector();
                Vector checkpoint = nearestCheckpoint.getTrackLocation().toVector();

                d += playerPos.subtract(checkpoint).length() + (lapCount - (playersPoints.get(playerUuid))) * trackLength;

                playerDistances.add(new PlayerDistance(playerUuid, d));
            }
        }

        Collections.sort(playerDistances);

        return playerDistances;
    }

    private boolean havePlayersFinished(HashMap<UUID, Integer> playersPoints) {
        for (UUID playerUuid : playersPoints.keySet()) {
            if (playersPoints.get(playerUuid) < lapCount) {
                return false;
            }
        }

        return true;
    }

    public void stop() {
        running = false;
    }

    public boolean addPlayer(Player player) {
        return players.add(player.getUniqueId());
    }

    public boolean removePlayer(Player player) {
        return players.remove(player.getUniqueId());
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
}
