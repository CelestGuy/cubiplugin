package net.ddns.lagarderie.cubiplugin.commands;

import net.ddns.lagarderie.cubiplugin.commands.subcommands.*;
import net.ddns.lagarderie.cubiplugin.game.Racing;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.ddns.lagarderie.cubiplugin.utils.CommandUtils.getStrings;
import static net.ddns.lagarderie.cubiplugin.utils.CommandUtils.handleCommand;

public class CommandGame implements TabExecutor {
    private final Map<String, TabExecutor> subCommands;

    public CommandGame() {
        subCommands = new HashMap<>();

        subCommands.put("start", new CommandGameStart());
        subCommands.put("stop", new CommandGameStop());
        subCommands.put("speed", new CommandGameSpeed());
        subCommands.put("laps", new CommandGameLaps());
        subCommands.put("track", new CommandGameTrack());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            if (commandSender instanceof Player player) {
                StringBuilder sb = new StringBuilder();

                Racing game = Racing.getInstance();

                sb.append("Paramètres du jeu: ")
                        .append("\n   - Course : ");
                if (game.getTrack() == null) {
                    sb.append("non choisie");
                } else {
                    sb.append(game.getTrack().getName());
                }

                sb.append("\n   - Vitesse : ").append(game.getSpeed()).append(" cc");
                sb.append("\n   - Nombre de tours : ").append(game.getLapCount());

                player.sendMessage(sb.toString());
            }
        } else {
            return handleCommand(commandSender, command, s, strings, subCommands);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return getStrings(commandSender, command, s, strings, subCommands);
    }
}
