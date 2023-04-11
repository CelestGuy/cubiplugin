package net.ddns.lagarderie.cubiplugin.game;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import static net.ddns.lagarderie.cubiplugin.RacingPlugin.*;

import java.util.HashSet;

public class Racing {
    private final HashSet<String> players;
    private String mapName = null;
    private boolean mirror = false;
    private Speed speed = Speed.CC150;
    private int lapCount = 3;

    public Racing() {
        players = new HashSet<>();
    }


    public boolean addPlayer(Player player) {
        return players.add(player.getName());
    }

    public boolean removePlayer(Player player) {
        return players.remove(player.getName());
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    public boolean getMirror() {
        return mirror;
    }

    public Speed getSpeed() {
        return speed;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public int getLapCount() {
        return lapCount;
    }

    public void setLapCount(int lapCount) {
        this.lapCount = lapCount;
    }

    public void init() throws RacingGameException {
        Server server = getInstance().getServer();
        ScoreboardManager sm = server.getScoreboardManager();

        if (mapName == null) {
            throw new RacingGameException("No map is selected");
        }
        // Create team
        else if (sm != null) {
            Scoreboard s = sm.getNewScoreboard();

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

    }

    public void destroy() {
        Server server = getInstance().getServer();
        ScoreboardManager sm = server.getScoreboardManager();

        // Create team
        if (sm != null) {
            Scoreboard s = sm.getNewScoreboard();

            s.getTeams().clear();
        }
    }
}
