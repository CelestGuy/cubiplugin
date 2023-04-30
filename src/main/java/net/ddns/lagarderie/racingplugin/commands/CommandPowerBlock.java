package net.ddns.lagarderie.racingplugin.commands;

import net.ddns.lagarderie.racingplugin.commands.subcommands.*;
import net.ddns.lagarderie.racingplugin.plugin.RacingCommandException;
import net.ddns.lagarderie.racingplugin.plugin.SafeCommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandPowerBlock extends SafeCommandExecutor {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return getStrings(commandSender, command, s, strings);
    }

    @Override
    public boolean executeSafeCommand(CommandSender commandSender, Command command, String s, String[] strings) throws RacingCommandException {
        return handleCommand(commandSender, command, s, strings);
    }

    @Override
    public Map<String, TabExecutor> getSubcommands() {
        HashMap<String, TabExecutor> subcommands = new HashMap<>();

        subcommands.put("add", new CommandPowerBlockAdd());
        subcommands.put("list", new CommandPowerBlockList());
        subcommands.put("remove", new CommandPowerBlockRemove());

        return subcommands;
    }
}
