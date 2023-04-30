package net.ddns.lagarderie.racingplugin.commands.subcommands;

import net.ddns.lagarderie.racingplugin.RacingPlugin;
import net.ddns.lagarderie.racingplugin.plugin.RacingCommandException;
import net.ddns.lagarderie.racingplugin.game.RacingGame;
import net.ddns.lagarderie.racingplugin.game.Track;
import net.ddns.lagarderie.racingplugin.plugin.RacingGameException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import java.util.ArrayList;
import java.util.List;

import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.getTrack;

public class CommandGameTrack implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            String arg = strings[0];

            RacingGame game = RacingGame.getInstance();

            if (game.isRunning()) {
                throw new RacingCommandException("Impossible de modifier les paramètres du jeu.");
            } else {
                try {
                    Track track = getTrack(arg);
                    String trackName = track.getName();
                    String trackId = track.getId();

                    game.setTrack(trackId);
                    commandSender.sendMessage("Course choisie : " + trackName);
                } catch (RacingGameException e) {
                    throw new RacingCommandException(e.getMessage());
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            ArrayList<String> tracksName = new ArrayList<>();
            for (Track track : RacingPlugin.getPlugin().getTracks()) {
                tracksName.add(track.getName());
            }

            return tracksName;
        }

        return List.of();
    }
}