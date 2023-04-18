package net.ddns.lagarderie.cubiplugin.game;

import net.ddns.lagarderie.cubiplugin.RacingPlugin;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static net.ddns.lagarderie.cubiplugin.utils.TrackUtils.loadTracks;

public class Racing {
    private static Racing instance = null;
    private final ArrayList<Track> tracks;
    private final HashSet<String> players;
    private Track track = null;
    private boolean mirror = false;
    private int speed = 150;
    private int lapCount = 3;

    private Racing() {
        players = new HashSet<>();
        tracks = loadTracks();
    }

    public static Racing getInstance() {
        if (instance == null) {
            instance = new Racing();
        }
        return instance;
    }

    public void init() throws RacingGameException {
        Server server = RacingPlugin.getInstance().getServer();
        ScoreboardManager sm = server.getScoreboardManager();

        if (track == null) {
            throw new RacingGameException("No map is selected");
        }
        // Create team
        else if (sm != null) {
            Scoreboard s = sm.getNewScoreboard();

            for (Player player : server.getOnlinePlayers()) {
                players.add(player.getName());
            }

            s.registerNewTeam("racers");
            Team racerTeam = s.getTeam("racers");

            if (racerTeam != null) {
                racerTeam.setColor(ChatColor.DARK_AQUA);
                racerTeam.setAllowFriendlyFire(false);
                racerTeam.setPrefix("{\"text\":\"[Racers] \",\"color\":\"dark_aqua\"}");

                for (String playerName : players) {
                    racerTeam.addEntry(playerName);
                }
            } else {
                throw new RacingGameException("Cannot create game");
            }
        }
    }

    public void start() {
        World world = RacingPlugin.getInstance().getServer().getWorld(track.getMapId());

        for (String playerName : players) {
            Player player = RacingPlugin.getInstance().getServer().getPlayer(playerName);

            if (player != null) {
                player.teleport(new Location(world, 0, 61, 0));
                player.setInvisible(true);
            }
        }

        BukkitScheduler scheduler = Bukkit.getScheduler();

        HashMap<String, Integer> playerCount = new HashMap<>();
        for (String player : players) {
            playerCount.put(player, 0);
        }

        final int[] timer = {3};
        final int[] counter = {0};

        scheduler.runTaskTimer(RacingPlugin.getInstance(), task -> {
            if (havePlayersFinished(playerCount)) {
                task.cancel();
            }

            if (timer[0] > 0) {
                if (counter[0] > 20L) {
                    timer[0]--;
                    counter[0] = 0;
                }

                for (String playerName : players) {
                    Player player = RacingPlugin.getInstance().getServer().getPlayer(playerName);

                    if (player != null) {
                        player.teleport(new Location(world, 0, 61, 0));
                        player.setWalkSpeed(0.2f);
                        playerCount.put(playerName, 0);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, (speed / 10), true));
                        player.sendTitle("" + timer[0], "", 0, 20, 0);
                    }
                }
            } else {
                for (String playerName : players) {
                    Player player = RacingPlugin.getInstance().getServer().getPlayer(playerName);

                    if (player != null && world != null) {
                        player.setInvisible(false);

                        Block block = world.getBlockAt(player.getLocation().subtract(new Vector(0, 1, 0)));

                        if (block.getType() == Material.YELLOW_CONCRETE) {
                            /*Location location = player.getLocation();
                            FireworkEffect options = FireworkEffect.builder().with(FireworkEffect.Type.STAR).withColor(Color.YELLOW).build();

                            world.spawnParticle(
                                    Particle.FIREWORKS_SPARK,
                                    location.getX(),
                                    location.getY(),
                                    location.getZ(),
                                    10,
                                    0,
                                    2,
                                    0,
                                    options
                            );*/

                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3, (speed / 10) + 5));
                        } else if (block.getType() == Material.GREEN_CONCRETE) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 3, 5));
                        }
                    }
                }
            }

            counter[0] += 1;
        }, 0L, 1L);
    }

    private boolean havePlayersFinished(HashMap<String, Integer> playerCount) {
        for (String playerName : playerCount.keySet()) {
            int count = playerCount.get(playerName);

            if (count != lapCount * track.getCheckpoints().size()) {
                return false;
            }
        }

        return true;
    }

    public void destroy() {
        Server server = RacingPlugin.getInstance().getServer();
        ScoreboardManager sm = server.getScoreboardManager();

        // Create team
        if (sm != null) {
            Scoreboard s = sm.getNewScoreboard();

            s.getTeams().clear();
        }
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public boolean addPlayer(Player player) {
        return players.add(player.getName());
    }

    public boolean removePlayer(Player player) {
        return players.remove(player.getName());
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return track;
    }

    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    public boolean getMirror() {
        return mirror;
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
