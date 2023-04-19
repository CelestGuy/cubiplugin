package net.ddns.lagarderie.cubiplugin.commands.game;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.ddns.lagarderie.cubiplugin.utils.CommandUtils.getStrings;
import static net.ddns.lagarderie.cubiplugin.utils.CommandUtils.handleCommand;

public class CommandGame implements TabExecutor {
    private static final Map<String, TabExecutor> commandArgs = new HashMap<>();

    public CommandGame() {
        commandArgs.put("start", new CommandGameStart());
        commandArgs.put("stop", new CommandGameStop());
        commandArgs.put("speed", new CommandGameSpeed());
        commandArgs.put("laps", new CommandGameLaps());
        commandArgs.put("track", new CommandGameTrack());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return handleCommand(commandSender, command, s, strings, commandArgs);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return getStrings(commandSender, command, s, strings, commandArgs);
    }
}
