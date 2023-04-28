package net.ddns.lagarderie.racingplugin.commands;

import net.ddns.lagarderie.racingplugin.plugin.SafeCommandExecutor;
import net.ddns.lagarderie.racingplugin.commands.subcommands.CommandCheckpointAdd;
import net.ddns.lagarderie.racingplugin.commands.subcommands.CommandCheckpointList;
import net.ddns.lagarderie.racingplugin.commands.subcommands.CommandCheckpointRadius;
import net.ddns.lagarderie.racingplugin.commands.subcommands.CommandCheckpointRemove;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandCheckpoint extends SafeCommandExecutor {
    @Override
    public boolean executeSafeCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return handleCommand(commandSender, command, s, strings);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return getStrings(commandSender, command, s, strings);
    }

    @Override
    public Map<String, TabExecutor> getSubcommands() {
        HashMap<String, TabExecutor> subcommands = new HashMap<>();

        subcommands.put("add", new CommandCheckpointAdd());
        subcommands.put("list", new CommandCheckpointList());
        subcommands.put("radius", new CommandCheckpointRadius());
        subcommands.put("remove", new CommandCheckpointRemove());

        return subcommands;
    }
}
