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

public class CommandCheckpointRemove implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            String worldName = player.getWorld().getName();

            for (Track track : Racing.getInstance().getTracks()) {
                if (track.getMapId().equals(worldName)) {
                    Location location = player.getLocation();

                    for (TrackLocation tl : track.getCheckpoints()) {
                        if ((int) (tl.getX()) == (int) (location.getX())
                                && (int) (tl.getY()) == (int) (location.getY())
                                && (int) (tl.getZ()) == (int) (location.getZ())) {
                            if (track.getCheckpoints().remove(tl)) {
                                player.sendMessage("Checkpoint (" +
                                        "§4" + tl.getX() +
                                        "§r/§2" + tl.getY() +
                                        "§r/§9" + tl.getZ() + "§r)§c supprimé§r !"
                                );

                                saveTrack(track);
                                return true;
                            } else {
                                throw new RacingCommandException("Le checkpoint n'existe pas !");
                            }
                        }
                    }
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
