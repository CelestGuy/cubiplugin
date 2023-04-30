package net.ddns.lagarderie.racingplugin.commands.subcommands;

import net.ddns.lagarderie.racingplugin.game.Checkpoint;
import net.ddns.lagarderie.racingplugin.game.Track;
import net.ddns.lagarderie.racingplugin.plugin.RacingCommandException;
import net.ddns.lagarderie.racingplugin.plugin.RacingGameException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.*;

public class CommandCheckpointTeleport implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            String worldName = player.getWorld().getName();

            Track track;
            Checkpoint checkpoint;

            int checkpointId = -1;

            try {
                track = getTrack(worldName);

                if (strings.length == 1) {
                    checkpointId = Integer.parseInt(strings[0]);
                    checkpoint = track.getCheckpoint(checkpointId);
                } else {
                    checkpoint = getClosestCheckpoint(player, track);
                }
            } catch (RacingGameException | NumberFormatException e) {
                throw new RacingCommandException(e.getMessage());
            }

            if (checkpoint != null) {
                player.teleport(getCheckpointLocation(player.getWorld(), checkpoint));
                player.sendMessage("Téléporté au checkpoint : " + checkpoint);
            }

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1 && commandSender instanceof Player player) {
            ArrayList<String> checkpointIds = new ArrayList<>();
            String arg = strings[0];

            try {
                for (Checkpoint checkpoint : getTrack(player.getWorld().getName()).getCheckpoints()) {
                    String id = String.valueOf(checkpoint.getId());
                    if (id.contains(arg)) {
                        checkpointIds.add(arg);
                    }
                }
            } catch (RacingGameException ignored) {}

            return checkpointIds;
        }

        return List.of();
    }
}
