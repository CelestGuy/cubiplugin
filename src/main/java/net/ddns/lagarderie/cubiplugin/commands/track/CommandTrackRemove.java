package net.ddns.lagarderie.cubiplugin.commands.track;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import net.ddns.lagarderie.cubiplugin.game.Racing;
import net.ddns.lagarderie.cubiplugin.game.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static net.ddns.lagarderie.cubiplugin.utils.TrackSaveUtils.saveTrack;

public class CommandTrackRemove implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            for (Track track : Racing.getInstance().getTracks()) {
                if (track.getMapId().equals(player.getWorld().getName())) {
                    if (Racing.getInstance().getTracks().remove(track)) {
                        player.sendMessage("La course a été supprimée");

                        saveTrack(track);
                        return true;
                    } else {
                        throw new RacingCommandException("Une erreur est survenue lors de la suppression de la course");
                    }
                }
            }

            throw new RacingCommandException("Ce monde ne contient pas de fichier de course.");
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}
