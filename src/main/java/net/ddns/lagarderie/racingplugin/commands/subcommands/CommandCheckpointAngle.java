package net.ddns.lagarderie.racingplugin.commands.subcommands;

import net.ddns.lagarderie.racingplugin.game.Checkpoint;
import net.ddns.lagarderie.racingplugin.game.Track;
import net.ddns.lagarderie.racingplugin.plugin.RacingCommandException;
import net.ddns.lagarderie.racingplugin.plugin.RacingGameException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.getClosestCheckpoint;
import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.getTrack;

public class CommandCheckpointAngle implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player && strings.length == 1) {
            String worldName = player.getWorld().getName();
            float angle;

            Track track;
            Checkpoint checkpoint;

            try {
                track = getTrack(worldName);
                checkpoint = getClosestCheckpoint(player, track);
                angle = Float.parseFloat(strings[0]);
            } catch (RacingGameException | NumberFormatException e) {
                throw new RacingCommandException(e.getMessage());
            }

            if (checkpoint != null) {
                checkpoint.setAngle(angle);
                player.sendMessage("Angle du checkpoint : " + checkpoint);
            }

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}
