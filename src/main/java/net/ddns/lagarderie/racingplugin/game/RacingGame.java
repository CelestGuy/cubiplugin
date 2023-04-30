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

import java.util.*;

import static net.ddns.lagarderie.racingplugin.RacingPlugin.getPlugin;
import static net.ddns.lagarderie.racingplugin.utils.ParticleUtils.drawCircle;
import static net.ddns.lagarderie.racingplugin.utils.ParticleUtils.spawnRedstoneParticle;
import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.*;
import static org.bukkit.Bukkit.getServer;

public class RacingGame {
    private static RacingGame instance = null;
    private final HashSet<UUID> playersCheckpointVisibility;
    private String trackId;
    private int speed;
    private int lapCount;
    private boolean running;

    private RacingGame() {
        this.playersCheckpointVisibility = new HashSet<>();
        this.trackId = null;
        this.speed = 100;
        this.lapCount = 3;
        this.running = false;
    }

    public static RacingGame getInstance() {
        if (instance == null) {
            instance = new RacingGame();
        }
        return instance;
    }

    public static void setInstance(RacingGame newInstance) {
        instance = newInstance;
    }

    public void start() throws RacingGameException {
        if (trackId == null) {
            throw new RacingGameException("Aucune course n'a été choisie.");
        } else if (running) {
            throw new RacingGameException("Un jeu est déjà en cours.");
        }

        Track track = getTrack(trackId);

        this.running = true;
        LinkedList<RacingPlayer> racingPlayers = new LinkedList<>();

        World world = getServer().getWorld(track.getId());

        BukkitScheduler scheduler = Bukkit.getScheduler();

        final int[] timer = {3};
        final int[] counter = {0};

        for (Player player : getServer().getOnlinePlayers()) {
            racingPlayers.add(new RacingPlayer(player.getUniqueId()));
        }

        scheduler.runTaskTimer(getPlugin(), counterTask -> {
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
                run(racingPlayers, track);
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

    public void run(LinkedList<RacingPlayer> racingPlayers, Track track) {
        World world = getServer().getWorld(track.getId());

        BukkitScheduler scheduler = Bukkit.getScheduler();

        final int[] counter = {0};

        for (RacingPlayer racingPlayer : racingPlayers) {
            racingPlayer.setCheckedLaps(0);
            racingPlayer.setCheckedCheckpoints(0);
        }

        float trackLength = getShortestDistance(track, track.getCheckpoint(track.getDepartureCheckpoint()), track.getCheckpoint(track.getArrivalCheckpoint()));
        float totalTrackLength = trackLength * (float) (lapCount);


        scheduler.runTaskTimer(getPlugin(), gameTask -> {
            if (!running || world == null) {
                gameTask.cancel();
            } else {
                int speedEffectAmplifier = Math.min(speed / 10, 255);

                sortPlayers(racingPlayers, track, trackLength, totalTrackLength);

                for (int i = 0; i < racingPlayers.size(); i++) {
                    RacingPlayer racingPlayer = racingPlayers.get(i);
                    Player player = getServer().getPlayer(racingPlayer.getUuid());

                    if (player != null) {
                        Block block = world.getBlockAt(player.getLocation().subtract(new Vector(0, 1, 0)));

                        ArrayList<PowerBlock> powerBlocks = PowerBlocksManager.getInstance().getPowerBlocks();
                        for (PowerBlock powerBlock : powerBlocks) {
                            if (powerBlock.getBlockMaterial() == block.getType()) {
                                PotionEffect pe = powerBlock.getEffect(speedEffectAmplifier);

                                if (pe != null) {
                                    player.addPotionEffect(pe);
                                }

                                break;
                            }
                        }

                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1, speedEffectAmplifier, false));

                        String playerTime = String.format(Locale.US, "%.2f", (counter[0] / 20f));
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                                getPlayerPosition(i) + " - " + racingPlayer.getDistance() + "m - Temps : " + playerTime + " s"
                        ));

                        Checkpoint validatedCheckpoint = track.getCheckpoint(racingPlayer.getCheckedCheckpoints());

                        for (Integer childId : validatedCheckpoint.getChildren()) {
                            Checkpoint nextCheckpoint = track.getCheckpoint(childId);

                            if (nextCheckpoint != null) {
                                if (canPlayerSeeCheckpoints(player)) {
                                    spawnRedstoneParticle(player, nextCheckpoint.getPosition(), Color.YELLOW, 0.5f);
                                    drawCircle(player, nextCheckpoint.getPosition(), Color.TEAL, nextCheckpoint.getRadius());
                                }

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
                                    break;
                                }
                            } else if (!isPlayerInCheckpoint(player, validatedCheckpoint)) {
                                player.teleport(getCheckpointLocation(world, validatedCheckpoint));
                            }
                        }
                    } else {
                        racingPlayers.remove(racingPlayer);
                    }
                }

                counter[0]++;
                running = !havePlayersFinished(racingPlayers);
            }
        }, 0L, 1L);
    }

    private void sortPlayers(LinkedList<RacingPlayer> racingPlayers, Track track, float trackLength, float totalTrackLength) {
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

    private boolean havePlayersFinished(LinkedList<RacingPlayer> racingPlayers) {
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

    public String getTrackId() {
        return trackId;
    }

    public void setTrack(String trackId) {
        this.trackId = trackId;
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

    public boolean canPlayerSeeCheckpoints(Player player) {
        return this.playersCheckpointVisibility.contains(player.getUniqueId());
    }

    public boolean enableCheckpointVisibility(Player player) {
        return this.playersCheckpointVisibility.add(player.getUniqueId());
    }

    public boolean disableCheckpointVisibility(Player player) {
        return this.playersCheckpointVisibility.remove(player.getUniqueId());
    }
}
