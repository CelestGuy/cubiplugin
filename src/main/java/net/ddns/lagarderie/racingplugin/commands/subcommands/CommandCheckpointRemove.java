package net.ddns.lagarderie.racingplugin.commands.subcommands;

import net.ddns.lagarderie.racingplugin.plugin.RacingCommandException;
import net.ddns.lagarderie.racingplugin.plugin.RacingGameException;
import net.ddns.lagarderie.racingplugin.game.Checkpoint;
import net.ddns.lagarderie.racingplugin.game.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.getClosestCheckpoint;
import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.getTrack;

public class CommandCheckpointRemove implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            String worldName = player.getWorld().getName();
            int checkpointNum = -1;

            Track track;
            Checkpoint checkpoint;

            try {
                track = getTrack(worldName);
                checkpoint = getClosestCheckpoint(player, track);

                if (strings.length == 1 && !strings[0].equals("all")) {
                    checkpointNum = Integer.parseInt(strings[0]);
                }
            } catch (RacingGameException | NumberFormatException e) {
                throw new RacingCommandException(e.getMessage());
            }

            if (checkpointNum >= 0) {
                Checkpoint c = track.getCheckpoint(checkpointNum);
                track.removeCheckpoint(c);
                player.sendMessage(c + " §c supprimé !");
                return true;
            } else {
                if (strings.length == 1 && strings[0].equals("all")) {
                    track.setCheckpoints(new ArrayList<>());
                    player.sendMessage("Tous les checkpoints ont été supprimés");
                } else {
                    track.removeCheckpoint(checkpoint);
                    player.sendMessage(checkpoint + " §c supprimé !");
                    return true;
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
