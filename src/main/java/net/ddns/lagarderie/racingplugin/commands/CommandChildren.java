package net.ddns.lagarderie.racingplugin.commands;

import net.ddns.lagarderie.racingplugin.commands.subcommands.CommandChildrenAdd;
import net.ddns.lagarderie.racingplugin.commands.subcommands.CommandChildrenList;
import net.ddns.lagarderie.racingplugin.commands.subcommands.CommandChildrenRemove;
import net.ddns.lagarderie.racingplugin.plugin.SafeCommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandChildren extends SafeCommandExecutor {
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
        Map<String, TabExecutor> subcommands = new HashMap<>();

        subcommands.put("add", new CommandChildrenAdd());
        subcommands.put("remove", new CommandChildrenRemove());
        subcommands.put("list", new CommandChildrenList());

        return subcommands;
    }
}
