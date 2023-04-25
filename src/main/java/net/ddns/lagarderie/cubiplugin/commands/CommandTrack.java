package net.ddns.lagarderie.cubiplugin.commands;


import net.ddns.lagarderie.cubiplugin.commands.subcommands.*;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import net.ddns.lagarderie.cubiplugin.game.Racing;
import net.ddns.lagarderie.cubiplugin.game.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.ddns.lagarderie.cubiplugin.utils.CommandUtils.getStrings;
import static net.ddns.lagarderie.cubiplugin.utils.CommandUtils.handleCommand;

public class CommandTrack implements TabExecutor {
    private final Map<String, TabExecutor> subCommands;

    public CommandTrack() {
        subCommands = new HashMap<>();

        subCommands.put("create", new CommandTrackCreate());
        subCommands.put("debug", new CommandTrackDebug());
        subCommands.put("list", new CommandTrackList());
        subCommands.put("remove", new CommandTrackRemove());
        subCommands.put("departure", new CommandTrackDeparture());
        subCommands.put("arrival", new CommandTrackArrival());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            if (commandSender instanceof Player player) {
                for (Track track : Racing.tracks) {
                    if (track.getMapId().equals(player.getWorld().getName())) {
                        player.sendMessage("Course " + (track.getName()));
                        return true;
                    }
                }

                throw new RacingCommandException("Ce monde ne contient pas de fichier de course.");
            }
        }

        return handleCommand(commandSender, command, s, strings, subCommands);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return getStrings(commandSender, command, s, strings, subCommands);
    }
}
