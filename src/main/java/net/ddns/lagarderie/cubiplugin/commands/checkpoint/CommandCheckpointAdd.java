package net.ddns.lagarderie.cubiplugin.commands.checkpoint;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import net.ddns.lagarderie.cubiplugin.game.Checkpoint;
import net.ddns.lagarderie.cubiplugin.game.Racing;
import net.ddns.lagarderie.cubiplugin.game.Track;
import net.ddns.lagarderie.cubiplugin.game.TrackLocation;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static net.ddns.lagarderie.cubiplugin.utils.TrackUtils.getTrack;
import static net.ddns.lagarderie.cubiplugin.utils.TrackUtils.saveTrack;

public class CommandCheckpointAdd implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            String worldName = player.getWorld().getName();
            Track track;

            try {
                track = getTrack(worldName);
            } catch (RacingGameException e) {
                throw new RacingCommandException(e.getMessage());
            }

            Location location = player.getLocation();

            System.out.println(location.getX() < 0 ? -1 : 1);

            location.setX((int) (location.getX()) + (0.5 * (location.getX() < 0 ? -1 : 1)));
            location.setY((int) (location.getY()));
            location.setZ((int) (location.getZ()) + (0.5 * (location.getZ() < 0 ? -1 : 1)));

            for (Checkpoint checkpoint : track.getCheckpoints()) {
                int checkpointX = (int) (checkpoint.getTrackLocation().getX());
                int checkpointY = (int) (checkpoint.getTrackLocation().getY());
                int checkpointZ = (int) (checkpoint.getTrackLocation().getZ());
                int locationX = (int) (location.getX());
                int locationY = (int) (location.getY());
                int locationZ = (int) (location.getZ());

                if (checkpointX == locationX && checkpointY == locationY && checkpointZ == locationZ) {
                    throw new RacingCommandException("Ce checkpoint existe déjà !");
                }
            }

            Checkpoint checkpoint = new Checkpoint();
            checkpoint.setTrackLocation(new TrackLocation(location));

            if (strings.length == 1) {
                checkpoint.setValue(Integer.parseInt(strings[0]));
            } else {
                int maxNum = 0;

                for (Checkpoint c : track.getCheckpoints()) {
                    if (c.getValue() > maxNum) {
                        maxNum = c.getValue();
                    }
                }

                checkpoint.setValue(maxNum + 1);
            }

            track.getCheckpoints().add(checkpoint);
            player.sendMessage(checkpoint.toString() + "§a ajouté§r ! "
            );

            saveTrack(track);

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}
