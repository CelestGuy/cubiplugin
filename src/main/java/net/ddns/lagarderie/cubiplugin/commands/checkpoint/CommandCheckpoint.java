package net.ddns.lagarderie.cubiplugin.commands.checkpoint;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.ddns.lagarderie.cubiplugin.utils.CommandUtils.getStrings;
import static net.ddns.lagarderie.cubiplugin.utils.CommandUtils.handleCommand;

public class CommandCheckpoint implements TabExecutor {
    private static final Map<String, TabExecutor> commandArgs = new HashMap<>();

    public CommandCheckpoint() {
        commandArgs.put("add", new CommandCheckpointAdd());
        commandArgs.put("list", new CommandCheckpointList());
        commandArgs.put("remove", new CommandCheckpointRemove());
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
