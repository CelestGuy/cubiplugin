package net.ddns.lagarderie.racingplugin.commands.subcommands;

import net.ddns.lagarderie.racingplugin.game.CheckpointType;
import net.ddns.lagarderie.racingplugin.plugin.RacingCommandException;
import net.ddns.lagarderie.racingplugin.plugin.RacingGameException;
import net.ddns.lagarderie.racingplugin.game.Checkpoint;
import net.ddns.lagarderie.racingplugin.game.Track;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.ddns.lagarderie.racingplugin.utils.RacingGameUtils.saveTrack;
import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.*;

public class CommandCheckpointAdd implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            String worldName = player.getWorld().getName();
            Track track;

            float radius = 1f;
            int parentCheckpointId = -1;

            try {
                track = getTrack(worldName);
            } catch (RacingGameException e) {
                throw new RacingCommandException(e.getMessage());
            }

            int maxId = getMaxCheckpointId(track);

            if (strings.length >= 1) {
                radius = Float.parseFloat(strings[0]);
            }
            if (strings.length == 2) {
                parentCheckpointId = Integer.parseInt(strings[1]);
            } else {
                parentCheckpointId = maxId;
            }


            Location playerLocation = player.getLocation();
            Checkpoint checkpoint = new Checkpoint(
                    maxId + 1,
                    playerLocation.toVector(),
                    playerLocation.getPitch(),
                    playerLocation.getYaw(),
                    radius
            );

            track.getCheckpoints().add(checkpoint);
            player.sendMessage(checkpoint + ChatColor.GREEN.toString() + " ajouté" + ChatColor.RESET + " !");

            if (parentCheckpointId >= 0) {
                Checkpoint parentCheckpoint = track.getCheckpoint(parentCheckpointId);
                try {
                    parentCheckpoint.addChildCheckpoint(checkpoint.getId());
                } catch (RacingGameException e) {
                    throw new RacingCommandException(e.getMessage());
                }
            }

            saveTrack(track);

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 2 && commandSender instanceof Player player) {
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
