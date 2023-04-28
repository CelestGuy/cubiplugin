package net.ddns.lagarderie.racingplugin.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;
import java.util.Map;

public abstract class SafeCommandExecutor implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        try {
            return executeSafeCommand(commandSender, command, s, strings);
        } catch (RacingCommandException e) {
            commandSender.sendMessage(e.getMessage());
        }

        return false;
    }

    @Override
    public abstract List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings);

    public abstract boolean executeSafeCommand(CommandSender commandSender, Command command, String s, String[] strings) throws RacingCommandException;

    public boolean handleCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Map<String, TabExecutor> subcommands = getSubcommands();

        if (strings.length >= 1) {
            String arg = strings[0];

            String[] args = new String[strings.length - 1];
            System.arraycopy(strings, 1, args, 0, strings.length - 1);

            if (subcommands.containsKey(arg)) {
                return subcommands.get(strings[0]).onCommand(commandSender, command, s, args);
            } else {
                throw new RacingCommandException("Cet argument n'existe pas !");
            }
        }

        return false;
    }

    public List<String> getStrings(CommandSender commandSender, Command command, String s, String[] strings) {
        Map<String, TabExecutor> subcommands = getSubcommands();

        if (strings.length == 1) {
            return subcommands.keySet().stream().toList();
        } else if (strings.length > 1) {
            String arg = strings[0];

            if (subcommands.containsKey(arg)) {
                String[] args = new String[strings.length - 1];
                System.arraycopy(strings, 1, args, 0, strings.length - 1);

                return subcommands.get(arg).onTabComplete(commandSender, command, s, args);
            }
        }

        return List.of();
    }

    public abstract Map<String, TabExecutor> getSubcommands();
}
