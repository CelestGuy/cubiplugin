package net.ddns.lagarderie.cubiplugin.commands;

import net.ddns.lagarderie.cubiplugin.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.HashMap;
import java.util.List;

import static net.ddns.lagarderie.cubiplugin.utils.CommandUtils.getStrings;
import static net.ddns.lagarderie.cubiplugin.utils.CommandUtils.handleCommand;

public class CommandCheckpoint implements TabExecutor {
    private final HashMap<String, TabExecutor> subCommands;

    public CommandCheckpoint() {
        subCommands = new HashMap<>();

        subCommands.put("add", new CommandCheckpointAdd());
        subCommands.put("autoPlace", new CommandCheckpointAutoPlace());
        subCommands.put("list", new CommandCheckpointList());
        subCommands.put("radius", new CommandCheckpointRadius());
        subCommands.put("remove", new CommandCheckpointRemove());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return handleCommand(commandSender, command, s, strings, subCommands);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return getStrings(commandSender, command, s, strings, subCommands);
    }
}
