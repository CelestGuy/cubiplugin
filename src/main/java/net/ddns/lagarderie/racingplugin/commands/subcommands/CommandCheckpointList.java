package net.ddns.lagarderie.racingplugin.commands.subcommands;

import net.ddns.lagarderie.racingplugin.RacingPlugin;
import net.ddns.lagarderie.racingplugin.plugin.RacingCommandException;
import net.ddns.lagarderie.racingplugin.game.Checkpoint;
import net.ddns.lagarderie.racingplugin.game.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class CommandCheckpointList implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            String worldName = player.getWorld().getName();

            for (Track track : RacingPlugin.getRacingPlugin().getTracks()) {
                if (track.getMapId().equals(worldName)) {
                    List<Checkpoint> checkpoints = track.getCheckpoints();
                    int checkpointsCount = checkpoints.size();

                    if (checkpointsCount > 0) {
                        player.sendMessage("§aLa course contient §b" + checkpointsCount + "§r§a checkpoints§r !");

                        for (int i = 0; i < checkpointsCount; i++) {
                            Checkpoint checkpoint = checkpoints.get(i);
                            Vector position = checkpoint.getPosition();

                            player.sendMessage("Checkpoint §d" + i + "§r (" +
                                    "§4" + position.getX() +
                                    "§r/§2" + position.getY() +
                                    "§r/§9" + position.getZ() + "§r)" +
                                    ", successeurs: " + checkpoint.getChildren()
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
