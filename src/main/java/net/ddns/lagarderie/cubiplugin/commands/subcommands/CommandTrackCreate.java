package net.ddns.lagarderie.cubiplugin.commands.subcommands;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import net.ddns.lagarderie.cubiplugin.game.Racing;
import net.ddns.lagarderie.cubiplugin.game.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static net.ddns.lagarderie.cubiplugin.utils.TrackUtils.saveTrack;

public class CommandTrackCreate implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            Track track = new Track();
            track.setMapId(player.getWorld().getName());
            track.setName(player.getWorld().getName());

            if (Racing.tracks.add(track)) {
                player.sendMessage("La course a été créée");
            } else {
                throw new RacingCommandException("Une erreur est survenue lors de la création de la course");
            }

            saveTrack(track);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}
