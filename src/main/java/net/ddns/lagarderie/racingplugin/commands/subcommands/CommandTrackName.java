package net.ddns.lagarderie.racingplugin.commands.subcommands;

import net.ddns.lagarderie.racingplugin.game.Track;
import net.ddns.lagarderie.racingplugin.plugin.RacingGameException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.getTrack;

public class CommandTrackName implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            Track track;

            try {
                track = getTrack(player.getWorld().getName());
            } catch (RacingGameException e) {
                throw new RuntimeException(e);
            }

            if (strings.length == 1) {
                String name = strings[0];
                track.setName(name);
                player.sendMessage("Le nouveau nom de la course " + track.getId() + " est: " + ChatColor.LIGHT_PURPLE + track.getName() + ChatColor.RESET);
            } else {
                player.sendMessage(track.getId() + ": " + track.getName());
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}
