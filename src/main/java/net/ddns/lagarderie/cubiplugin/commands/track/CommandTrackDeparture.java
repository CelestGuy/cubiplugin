package net.ddns.lagarderie.cubiplugin.commands.track;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import net.ddns.lagarderie.cubiplugin.game.Checkpoint;
import net.ddns.lagarderie.cubiplugin.game.Racing;
import net.ddns.lagarderie.cubiplugin.game.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static net.ddns.lagarderie.cubiplugin.utils.CheckpointUtils.getClosestCheckpoint;
import static net.ddns.lagarderie.cubiplugin.utils.TrackUtils.getTrack;

public class CommandTrackDeparture implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            Track track = null;
            Checkpoint checkpoint = null;
            try {
                track = getTrack(player.getWorld().getName());
                checkpoint = getClosestCheckpoint(player, track);
            } catch (RacingGameException e) {
                throw new RuntimeException(e);
            }

            if (checkpoint != null) {
                track.setDepartureCheckpoint(checkpoint.getId());
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
