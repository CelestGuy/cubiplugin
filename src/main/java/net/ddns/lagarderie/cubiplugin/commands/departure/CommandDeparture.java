package net.ddns.lagarderie.cubiplugin.commands.departure;

import net.ddns.lagarderie.cubiplugin.commands.checkpoint.CommandCheckpointAdd;
import net.ddns.lagarderie.cubiplugin.commands.checkpoint.CommandCheckpointList;
import net.ddns.lagarderie.cubiplugin.commands.checkpoint.CommandCheckpointRemove;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.ddns.lagarderie.cubiplugin.utils.CommandUtils.getStrings;

public class CommandDeparture implements TabExecutor {
    private static final Map<String, TabExecutor> commandArgs = new HashMap<>();

    public CommandDeparture() {
        commandArgs.put("start", new CommandDepartureStart());
        commandArgs.put("end", new CommandDepartureEnd());
        commandArgs.put("remove", new CommandDepartureRemove());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length >= 1) {
                String arg = strings[0];

                String[] args = new String[strings.length - 1];
                System.arraycopy(strings, 1, args, 0, strings.length - 1);

                if (commandArgs.containsKey(arg)) {
                    try {
                        return commandArgs.get(strings[0]).onCommand(commandSender, command, s, args);
                    } catch (RacingCommandException e) {
                        player.sendMessage("§c" + e.getMessage() + "§r");
                        return false;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return getStrings(commandSender, command, s, strings, commandArgs);
    }
}
