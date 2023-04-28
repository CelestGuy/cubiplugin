package net.ddns.lagarderie.racingplugin.game;

import net.ddns.lagarderie.racingplugin.plugin.RacingGameException;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;
import java.util.UUID;

import static net.ddns.lagarderie.racingplugin.RacingPlugin.getRacingPlugin;
import static net.ddns.lagarderie.racingplugin.utils.ParticleUtils.drawCircle;
import static net.ddns.lagarderie.racingplugin.utils.ParticleUtils.spawnRedstoneParticle;
import static net.ddns.lagarderie.racingplugin.utils.TrackGraph.getShortestDistance;
import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.getCheckpointLocation;
import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.isPlayerInCheckpoint;
import static org.bukkit.Bukkit.getServer;

public class Racing {
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
        if (track == null) {
            throw new RacingGameException("Aucune course n'a été choisie.");
        } else if (running) {
            throw new RacingGameException("Un jeu est déjà en cours.");
        }

        this.running = true;
        this.racingPlayers = new LinkedList<>();

        World world = getServer().getWorld(track.getMapId());

        BukkitScheduler scheduler = Bukkit.getScheduler();

        final int[] timer = {3};
        final int[] counter = {0};

        for (Player player : getServer().getOnlinePlayers()) {
            racingPlayers.add(new RacingPlayer(player.getUniqueId()));
        }

        scheduler.runTaskTimer(getRacingPlugin(), counterTask -> {
            if (!running || timer[0] == 0) {
                counterTask.cancel();
                for (RacingPlayer racingPlayer : racingPlayers) {
                    UUID playerUuid = racingPlayer.getUuid();
                    Player player = getServer().getPlayer(playerUuid);

                    if (player != null) {
                        player.setInvisible(false);
                        player.sendTitle(ChatColor.GREEN + "Go !" + ChatColor.RESET, "", 0, 20, 0);
                    }
                }
                run();
            } else {
                Checkpoint firstCheckpoint;

                if (track.getCheckpoints().size() > 0) {
                    firstCheckpoint = track.getCheckpoint(track.getDepartureCheckpoint());

                    for (RacingPlayer racingPlayer : racingPlayers) {
                        UUID playerUuid = racingPlayer.getUuid();
                        Player player = getServer().getPlayer(playerUuid);

                        if (player != null) {
                            player.setInvisible(true);
                            player.teleport(getCheckpointLocation(world, firstCheckpoint));
                            player.sendTitle(String.valueOf(timer[0]), "", 0, 20, 0);
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
        float totalTrackLength = trackLength * (float) (lapCount);


        scheduler.runTaskTimer(getRacingPlugin(), gameTask -> {
            if (!running || world == null) {
                gameTask.cancel();
            } else {
                int speedEffect = Math.min(speed / 10, 255);

                sortPlayers(trackLength, totalTrackLength);

                for (int i = 0; i < racingPlayers.size(); i++) {
                    RacingPlayer racingPlayer = racingPlayers.get(i);
                    Player player = getServer().getPlayer(racingPlayer.getUuid());

                    if (player != null) {
                        Block block = world.getBlockAt(player.getLocation().subtract(new Vector(0, 1, 0)));

                        switch (block.getType()) {
                            case YELLOW_CONCRETE, YELLOW_TERRACOTTA -> {
                                int boostedSpeedEffect = Math.min((speedEffect + 5), 255);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10, boostedSpeedEffect, false));
                            }
                            case GREEN_CONCRETE ->
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5, 5, false));
                            case MAGENTA_CONCRETE ->
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 5, 15, false));
                        }

                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1, speedEffect, false));

                        String playerTime = String.format(Locale.US, "%.2f", (counter[0] / 20f));
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                                getPlayerPosition(i) + " - " + racingPlayer.getDistance() + "m - Temps : " + playerTime + " s"
                        ));

                        // validate checkpoints

                        Checkpoint validatedCheckpoint = track.getCheckpoint(racingPlayer.getCheckedCheckpoints());

                        for (Integer childId : validatedCheckpoint.getChildren()) {
                            Checkpoint nextCheckpoint = track.getCheckpoint(childId);

                            if (nextCheckpoint != null) {
                                spawnRedstoneParticle(player, nextCheckpoint.getPosition(), Color.YELLOW, 0.5f);
                                drawCircle(player, nextCheckpoint.getPosition(), Color.TEAL, nextCheckpoint.getRadius());

                                if (isPlayerInCheckpoint(player, nextCheckpoint)) {
                                    if (childId == track.getDepartureCheckpoint()) {
                                        int lap = racingPlayer.getCheckedLaps() + 1;

                                        if (lap == lapCount) {
                                            racingPlayer.setCheckedLaps(lap);
                                            player.sendTitle(ChatColor.GOLD + "Arrivé !!" + ChatColor.RESET, "Temps : " + playerTime + " s", 10, 60, 10);
                                        } else {
                                            racingPlayer.setCheckedLaps(lap);
                                            player.sendTitle((lap + 1) + "e tour", "", 10, 40, 10);
                                        }
                                    }
                                    racingPlayer.setCheckedCheckpoints(childId);
                                }

                                break;
                            } else if (!isPlayerInCheckpoint(player, validatedCheckpoint)) {
                                player.teleport(getCheckpointLocation(world, validatedCheckpoint));
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

    private void sortPlayers(float trackLength, float totalTrackLength) {
        Checkpoint arrival = track.getCheckpoint(track.getArrivalCheckpoint());

        for (RacingPlayer racingPlayer : racingPlayers) {
            UUID playerUuid = racingPlayer.getUuid();

            Player player = getServer().getPlayer(playerUuid);
            if (player != null) {
                Checkpoint validatedCheckpoint = track.getCheckpoint(racingPlayer.getCheckedCheckpoints());
                float distance = Float.MAX_VALUE;

                for (Integer childId : validatedCheckpoint.getChildren()) {
                    Checkpoint nextCheckpoint = track.getCheckpoint(childId);

                    if (nextCheckpoint != null) {
                        Vector playerPos = player.getLocation().toVector().clone();
                        Vector checkpointPos = nextCheckpoint.getPosition().clone();

                        float shortestDistance = 0f;

                        if (nextCheckpoint.getId() != track.getDepartureCheckpoint()) {
                             shortestDistance = getShortestDistance(track, nextCheckpoint, arrival);
                        }

                        float nextCheckpointDistance = (float) playerPos.subtract(checkpointPos).length();
                        float traveledDistance = (totalTrackLength - (racingPlayer.getCheckedLaps() + 1) * trackLength);

                        float d = shortestDistance
                                + nextCheckpointDistance
                                + traveledDistance;

                        distance = Math.min(distance, d);
                    }
                }

                racingPlayer.setDistance(distance);
            } else {
                racingPlayers.remove(racingPlayer);
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

    private String getPlayerPosition(int i) {
        StringBuilder sb = new StringBuilder();
        String suffix = "e";

        switch (i) {
            case 0 -> {
                sb.append(ChatColor.GOLD).append(ChatColor.BOLD);
                suffix = "er";
            }
            case 1 -> sb.append(ChatColor.GRAY);
            case 2 -> sb.append(ChatColor.RED);
            default -> sb.append(ChatColor.AQUA).append(ChatColor.ITALIC);
        }

        return sb.append(i + 1).append(suffix).append(ChatColor.RESET).toString();
    }

    public void stop() {
        running = false;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
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
