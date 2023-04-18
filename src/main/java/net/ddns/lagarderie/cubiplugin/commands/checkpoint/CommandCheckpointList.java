package net.ddns.lagarderie.cubiplugin.commands.checkpoint;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import net.ddns.lagarderie.cubiplugin.game.Checkpoint;
import net.ddns.lagarderie.cubiplugin.game.Racing;
import net.ddns.lagarderie.cubiplugin.game.Track;
import net.ddns.lagarderie.cubiplugin.game.TrackLocation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandCheckpointList implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            String worldName = player.getWorld().getName();

            for (Track track : Racing.getInstance().getTracks()) {
                if (track.getMapId().equals(worldName)) {
                    List<Checkpoint> checkpoints = track.getCheckpoints();
                    int checkpointsCount = checkpoints.size();

                    if (checkpointsCount > 0) {
                        player.sendMessage("§aLa course contient §b" + checkpointsCount + "§r§a checkpoints§r !");

                        for (int i = 0; i < checkpointsCount; i++) {
                            Checkpoint checkpoint = checkpoints.get(i);
                            TrackLocation location = checkpoint.getTrackLocation();

                            player.sendMessage("Checkpoint §d" + (i + 1) + "§r (" +
                                    "§4" + location.getX() +
                                    "§r/§2" + location.getY() +
                                    "§r/§9" + location.getZ() + "§r) avec de valeur " +
                                    "" + checkpoint.getValue()
                            );
                        }

                        return true;
                    } else {
                        throw new RacingCommandException("Cette course ne contient aucun checkpoint !");
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
