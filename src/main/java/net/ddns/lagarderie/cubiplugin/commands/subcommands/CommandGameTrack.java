package net.ddns.lagarderie.cubiplugin.commands.subcommands;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import net.ddns.lagarderie.cubiplugin.game.Racing;
import net.ddns.lagarderie.cubiplugin.game.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import java.util.ArrayList;
import java.util.List;

public class CommandGameTrack implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            String arg = strings[0];

            for (Track track : Racing.tracks) {
                if (arg.equals(track.getName())) {
                    Racing game = Racing.getInstance();

                    if (game.isRunning()) {
                        throw new RacingCommandException("Impossible de modifier les paramètres du jeu.");
                    } else {
                        game.setTrack(track);
                        commandSender.sendMessage("Course choisie : " + track.getName());
                        return true;
                    }
                }
            }

            throw new RacingCommandException("La course n'existe pas.");
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            ArrayList<String> tracksName = new ArrayList<>();
            for (Track track : Racing.tracks) {
                tracksName.add(track.getName());
            }

            return tracksName;
        }

        return List.of();
    }
}