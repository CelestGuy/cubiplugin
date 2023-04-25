package net.ddns.lagarderie.cubiplugin.utils;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class CommandUtils {
    public static boolean handleCommand(CommandSender commandSender, Command command, String s, String[] strings, Map<String, TabExecutor> commandArgs) {
        if (commandSender instanceof Player player) {
            if (strings.length >= 1) {
                String arg = strings[0];

                String[] args = new String[strings.length - 1];
                System.arraycopy(strings, 1, args, 0, strings.length - 1);

                if (commandArgs.containsKey(arg)) {
                    try {
                        return commandArgs.get(strings[0]).onCommand(commandSender, command, s, args);
                    } catch (RacingCommandException e) {
                        player.sendMessage(e.getMessage());
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Cet argument n'existe pas !" + ChatColor.RESET);
                }
            }
        }

        return false;
    }

    public static List<String> getStrings(CommandSender commandSender, Command command, String s, String[] strings, Map<String, TabExecutor> commandArgs) {
        if (strings.length == 1) {
            return commandArgs.keySet().stream().toList();
        } else if (strings.length > 1) {
            String arg = strings[0];

            if (commandArgs.containsKey(arg)) {
                String[] args = new String[strings.length - 1];
                System.arraycopy(strings, 1, args, 0, strings.length - 1);

                return commandArgs.get(arg).onTabComplete(commandSender, command, s, args);
            }
        }

        return List.of();
    }
}
