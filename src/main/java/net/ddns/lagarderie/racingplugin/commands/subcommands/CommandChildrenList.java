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

public class CommandChildrenList implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length == 0) {
                String worldName = player.getWorld().getName();

                Track track;
                Checkpoint checkpoint;

                try {
                    track = getTrack(worldName);
                    checkpoint = getClosestCheckpoint(player, track);

                    if (checkpoint != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Successeurs du checkpoint").append(checkpoint.getId()).append(" : ");

                        for (Integer childId : checkpoint.getChildren()) {
                            Checkpoint child = track.getCheckpoint(childId);
                            sb.append("\n   - ").append(child);
                        }

                        player.sendMessage(sb.toString());
                    }
                } catch (RacingGameException | NumberFormatException e) {
                    throw new RacingCommandException(e.getMessage());
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}
