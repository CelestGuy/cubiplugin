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
        if (strings.length < 3) {
            throw new RacingCommandException("Il n'y a pas assez d'arguments.");
        } else if (strings.length > 4) {
            throw new RacingCommandException("Il y a trop d'arguments.");
        }

        if (commandSender instanceof Player player) {
            String worldName = player.getWorld().getName();
            Track track;

            try {
                track = getTrack(worldName);
            } catch (RacingGameException e) {
                throw new RacingCommandException(e.getMessage());
            }

            int maxId = getMaxCheckpointId(track);

            CheckpointType type = CheckpointType.parseType(strings[0]);
            float radius = Float.parseFloat(strings[1]);
            float angle = Float.parseFloat(strings[2]);
            int parentCheckpointId = maxId;

            if (strings.length == 4) {
                parentCheckpointId = Integer.parseInt(strings[1]);
            }

            Location playerLocation = player.getLocation();
            Checkpoint checkpoint = new Checkpoint(
                    maxId + 1,
                    type,
                    playerLocation.toVector(),
                    playerLocation.getYaw(),
                    playerLocation.getPitch(),
                    radius,
                    angle
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
        if (commandSender instanceof Player player) {
            if (strings.length == 1) {
                ArrayList<String> types = new ArrayList<>();

                for (CheckpointType type : CheckpointType.values()) {
                    types.add(type.toString());
                }

                return types;
            } else if (strings.length == 3) {
                ArrayList<String> checkpointIds = new ArrayList<>();
                String arg = strings[0];

                try {
                    for (Checkpoint checkpoint : getTrack(player.getWorld().getName()).getCheckpoints()) {
                        String id = String.valueOf(checkpoint.getId());
                        if (id.contains(arg)) {
                            checkpointIds.add(id);
                        }
                    }
                } catch (RacingGameException ignored) {}

                return checkpointIds;
            }
        }

        return List.of();
    }
}
