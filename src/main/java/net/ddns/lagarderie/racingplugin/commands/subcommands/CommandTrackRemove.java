package net.ddns.lagarderie.racingplugin.commands.subcommands;

import net.ddns.lagarderie.racingplugin.RacingPlugin;
import net.ddns.lagarderie.racingplugin.plugin.RacingCommandException;
import net.ddns.lagarderie.racingplugin.game.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static net.ddns.lagarderie.racingplugin.utils.RacingGameUtils.saveTrack;

public class CommandTrackRemove implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            for (Track track : RacingPlugin.getPlugin().getTracks()) {
                if (track.getId().equals(player.getWorld().getName())) {
                    if (RacingPlugin.getPlugin().getTracks().remove(track)) {
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
