package net.ddns.lagarderie.racingplugin.commands.subcommands;

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

import java.util.List;

import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.*;

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

            Checkpoint checkpoint = new Checkpoint();

            int maxId = getMaxCheckpointId(track);

            Location playerLocation = player.getLocation();

            checkpoint.setPosition(playerLocation.toVector());
            checkpoint.setPitch(playerLocation.getPitch());
            checkpoint.setYaw(playerLocation.getYaw());
            checkpoint.setId(maxId + 1);

            track.getCheckpoints().add(checkpoint);
            player.sendMessage(checkpoint + ChatColor.GREEN.toString() + " ajouté" + ChatColor.RESET + " !");

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
