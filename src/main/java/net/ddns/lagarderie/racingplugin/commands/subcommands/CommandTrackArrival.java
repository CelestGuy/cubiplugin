package net.ddns.lagarderie.racingplugin.commands.subcommands;

import net.ddns.lagarderie.racingplugin.plugin.RacingCommandException;
import net.ddns.lagarderie.racingplugin.plugin.RacingGameException;
import net.ddns.lagarderie.racingplugin.game.Checkpoint;
import net.ddns.lagarderie.racingplugin.game.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.getClosestCheckpoint;
import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.getTrack;

public class CommandTrackArrival implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            Track track;
            Checkpoint checkpoint;

            try {
                track = getTrack(player.getWorld().getName());
                checkpoint = getClosestCheckpoint(player, track);
            } catch (RacingGameException e) {
                throw new RuntimeException(e);
            }

            if (checkpoint != null) {
                track.setArrivalCheckpoint(checkpoint.getId());
            } else {
                throw new RacingCommandException("Cette course ne contient aucun checkpoint");
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}
