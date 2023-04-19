package net.ddns.lagarderie.cubiplugin.commands.checkpoint;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import net.ddns.lagarderie.cubiplugin.game.Checkpoint;
import net.ddns.lagarderie.cubiplugin.game.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static net.ddns.lagarderie.cubiplugin.utils.CheckpointUtils.getClosestCheckpoint;
import static net.ddns.lagarderie.cubiplugin.utils.TrackUtils.getTrack;

public class CommandCheckpointNextCheckpoint implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player && strings.length == 1) {
            String worldName = player.getWorld().getName();
            int value = 0;

            Track track;
            Checkpoint checkpoint;

            try {
                track = getTrack(worldName);
                checkpoint = getClosestCheckpoint(player, track);
                value = Integer.parseInt(strings[0]);
            } catch (RacingGameException | NumberFormatException e) {
                throw new RacingCommandException(e.getMessage());
            }

            if (checkpoint != null) {
                checkpoint.addChildCheckpoint(value);
                player.sendMessage("Prochain checkpoint à atteindre : " + checkpoint);
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
