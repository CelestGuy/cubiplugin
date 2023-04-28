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

public class CommandChildrenRemove implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length == 1) {
                String worldName = player.getWorld().getName();

                Track track;
                Checkpoint checkpoint;

                int childId;

                try {
                    track = getTrack(worldName);
                    checkpoint = getClosestCheckpoint(player, track);
                    childId = Integer.parseInt(strings[0]);

                    if (checkpoint != null) {
                        checkpoint.removeChildCheckpoint(childId);
                        player.sendMessage("Checkpoint : " + checkpoint);
                        return true;
                    }
                } catch (RacingGameException | NumberFormatException e) {
                    throw new RacingCommandException(e.getMessage());
                }
            } else {
                throw new RacingCommandException("L'id n'était pas spécifié.");
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}
