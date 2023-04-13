package net.ddns.lagarderie.cubiplugin.commands.checkpoint;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import net.ddns.lagarderie.cubiplugin.game.Racing;
import net.ddns.lagarderie.cubiplugin.game.Track;
import net.ddns.lagarderie.cubiplugin.game.TrackLocation;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static net.ddns.lagarderie.cubiplugin.utils.TrackSaveUtils.saveTrack;

public class CommandCheckpointAdd implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            String worldName = player.getWorld().getName();

            for (Track track : Racing.getInstance().getTracks()) {
                if (track.getMapId().equals(worldName)) {
                    Location location = player.getLocation();

                    if (location.getX() < 0) {
                        location.setX((int) (location.getX()) - 0.5);
                    } else {
                        location.setX((int) (location.getX()) + 0.5);
                    }

                    location.setY((int) (location.getY()));

                    if (location.getZ() < 0) {
                        location.setZ((int) (location.getZ()) - 0.5);
                    } else {
                        location.setZ((int) (location.getZ()) + 0.5);
                    }

                    for (TrackLocation tl : track.getCheckpoints()) {
                        if ((int) (tl.getX()) == (int) (location.getX())
                                && (int) (tl.getY()) == (int) (location.getY())
                                && (int) (tl.getZ()) == (int) (location.getZ())) {
                            throw new RacingCommandException("Ce checkpoint existe déjà !");
                        }
                    }

                    track.getCheckpoints().add(new TrackLocation(location));
                    player.sendMessage("Checkpoint (" +
                            "§4" + location.getX() +
                            "§r/§2" + location.getY() +
                            "§r/§9" + location.getZ() + "§r)§a ajouté§r !"
                    );

                    saveTrack(track);

                    return true;
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
