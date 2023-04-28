package net.ddns.lagarderie.racingplugin.commands;

import net.ddns.lagarderie.racingplugin.plugin.SafeCommandExecutor;
import net.ddns.lagarderie.racingplugin.commands.subcommands.*;
import net.ddns.lagarderie.racingplugin.game.Racing;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandGame extends SafeCommandExecutor {
    @Override
    public boolean executeSafeCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            if (commandSender instanceof Player player) {
                StringBuilder sb = new StringBuilder();

                Racing game = Racing.getInstance();

                sb.append("Paramètres du jeu: ")
                        .append("\n   - Course : ");
                if (game.getTrack() == null) {
                    sb.append(ChatColor.RED);
                    sb.append("Non choisie");
                    sb.append(ChatColor.RESET);
                } else {
                    sb.append(game.getTrack().getName());
                }

                sb.append("\n   - Vitesse : ").append(game.getSpeed()).append(" cc");
                sb.append("\n   - Nombre de tours : ").append(game.getLapCount());

                player.sendMessage(sb.toString());
            }
        } else {
            return handleCommand(commandSender, command, s, strings);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return getStrings(commandSender, command, s, strings);
    }

    @Override
    public Map<String, TabExecutor> getSubcommands() {
        Map<String, TabExecutor> subcommands = new HashMap<>();

        subcommands.put("start", new CommandGameStart());
        subcommands.put("stop", new CommandGameStop());
        subcommands.put("speed", new CommandGameSpeed());
        subcommands.put("laps", new CommandGameLaps());
        subcommands.put("track", new CommandGameTrack());

        return subcommands;
    }
}
