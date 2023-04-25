package net.ddns.lagarderie.cubiplugin.commands.subcommands;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import net.ddns.lagarderie.cubiplugin.game.Checkpoint;
import net.ddns.lagarderie.cubiplugin.game.Track;
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

            Checkpoint checkpoint = new Checkpoint();
            Location location = player.getLocation().clone();

            location.setPitch(player.getLocation().getPitch());
            location.setYaw(player.getLocation().getYaw());

            int maxId = -1;
            for (Checkpoint c : track.getCheckpoints()) {
                if (c.getId() > maxId) {
                    maxId = c.getId();
                }
            }

            checkpoint.setId(maxId + 1);

            checkpoint.setLocation(location);

            track.getCheckpoints().add(checkpoint);
            player.sendMessage(checkpoint + "§a ajouté§r ! ");

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
