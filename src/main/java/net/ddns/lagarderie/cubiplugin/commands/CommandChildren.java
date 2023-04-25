package net.ddns.lagarderie.cubiplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.HashMap;
import java.util.List;

import static net.ddns.lagarderie.cubiplugin.utils.CommandUtils.getStrings;
import static net.ddns.lagarderie.cubiplugin.utils.CommandUtils.handleCommand;

public class CommandChildren implements TabExecutor {
    private final HashMap<String, TabExecutor> subCommands;

    public CommandChildren() {
        subCommands = new HashMap<>();

        subCommands.put("add", null);
        subCommands.put("remove", null);
        subCommands.put("list", null);
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
