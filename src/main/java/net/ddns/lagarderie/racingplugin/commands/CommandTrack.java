package net.ddns.lagarderie.racingplugin.commands;


import net.ddns.lagarderie.racingplugin.RacingPlugin;
import net.ddns.lagarderie.racingplugin.plugin.SafeCommandExecutor;
import net.ddns.lagarderie.racingplugin.commands.subcommands.*;
import net.ddns.lagarderie.racingplugin.plugin.RacingCommandException;
import net.ddns.lagarderie.racingplugin.game.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandTrack extends SafeCommandExecutor {
    @Override
    public boolean executeSafeCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            if (commandSender instanceof Player player) {
                for (Track track : RacingPlugin.getRacingPlugin().getTracks()) {
                    if (track.getMapId().equals(player.getWorld().getName())) {
                        player.sendMessage("Course " + (track.getName()));
                        return true;
                    }
                }

                throw new RacingCommandException("Ce monde ne contient pas de fichier de course.");
            }
        }

        return handleCommand(commandSender, command, s, strings);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return getStrings(commandSender, command, s, strings);
    }

    @Override
    public Map<String, TabExecutor> getSubcommands() {
        HashMap<String, TabExecutor> subcommands = new HashMap<>();

        subcommands.put("create", new CommandTrackCreate());
        subcommands.put("list", new CommandTrackList());
        subcommands.put("remove", new CommandTrackRemove());
        subcommands.put("departure", new CommandTrackDeparture());
        subcommands.put("arrival", new CommandTrackArrival());

        return subcommands;
    }
}
