package net.ddns.lagarderie.cubiplugin.commands;


import net.ddns.lagarderie.cubiplugin.commands.track.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class CommandTrack implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length >= 1) {
            String[] args = new String[strings.length - 1];
            if (strings.length >= 2) {
                System.arraycopy(strings, 1, args, 0, strings.length - 1);
            }

            switch (strings[0]) {
                case "debug" -> {
                    return new CommandDebug().onCommand(commandSender, command, strings[0], args);
                }
                case "create" -> {
                    return new CommandCreate().onCommand(commandSender, command, strings[0], args);
                }
                case "remove" -> {
                    return new CommandRemove().onCommand(commandSender, command, strings[0], args);
                }
                case "list" -> {
                    return new CommandList().onCommand(commandSender, command, strings[0], args);
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length > 1) {
            String[] args = new String[strings.length - 1];

            switch (strings[0]) {
                case "debug" -> {
                    return new CommandDebug().onTabComplete(commandSender, command, strings[0], args);
                }
                case "create" -> {
                    return new CommandCreate().onTabComplete(commandSender, command, strings[0], args);
                }
                case "remove" -> {
                    return new CommandRemove().onTabComplete(commandSender, command, strings[0], args);
                }
                case "list" -> {
                    return new CommandList().onTabComplete(commandSender, command, strings[0], args);
                }
            }
        } else if (strings.length == 1) {
            return List.of("debug", "create", "remove", "list");
        }

        return List.of();
    }
}
