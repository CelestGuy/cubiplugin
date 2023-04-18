package net.ddns.lagarderie.cubiplugin.commands.game;

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

            for (Track track : Racing.getInstance().getTracks()) {
                if (arg.equals(track.getName())) {
                    Racing.getInstance().setTrack(track);
                    commandSender.sendMessage("La course choisie est : " + track.getName());
                    return true;
                }
            }

            throw new RacingCommandException("La course n'existe pas");
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            ArrayList<String> tracksName = new ArrayList<>();
            for (Track track : Racing.getInstance().getTracks()) {
                tracksName.add(track.getName());
            }

            return tracksName;
        }

        return List.of();
    }
}