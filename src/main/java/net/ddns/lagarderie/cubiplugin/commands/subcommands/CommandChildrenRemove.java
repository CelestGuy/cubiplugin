package net.ddns.lagarderie.cubiplugin.commands.subcommands;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import net.ddns.lagarderie.cubiplugin.game.Checkpoint;
import net.ddns.lagarderie.cubiplugin.game.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

import static net.ddns.lagarderie.cubiplugin.utils.CheckpointUtils.getClosestCheckpoint;
import static net.ddns.lagarderie.cubiplugin.utils.CommandUtils.getStrings;
import static net.ddns.lagarderie.cubiplugin.utils.CommandUtils.handleCommand;
import static net.ddns.lagarderie.cubiplugin.utils.TrackUtils.getTrack;

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
