package net.ddns.lagarderie.racingplugin.commands;

import net.ddns.lagarderie.racingplugin.plugin.RacingCommandException;
import net.ddns.lagarderie.racingplugin.plugin.SafeCommandExecutor;
import net.ddns.lagarderie.racingplugin.tools.ShowCheckpoints;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandShowcheckpoints extends SafeCommandExecutor {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            return List.of("enable", "disable");
        }

        return List.of();
    }

    @Override
    public boolean executeSafeCommand(CommandSender commandSender, Command command, String s, String[] strings) throws RacingCommandException {
        if (commandSender instanceof Player player) {
            ShowCheckpoints showCheckpoints = ShowCheckpoints.getPlayer(player.getUniqueId());

            if (strings.length == 1) {
                switch (strings[0]) {
                    case "disable" -> {
                        if (showCheckpoints.isEnabled()) {
                            player.sendMessage(ChatColor.YELLOW + "Affichage des checkpoints désactivé !" + ChatColor.RESET);
                            ShowCheckpoints.removePlayer(player.getUniqueId());
                        } else {
                            throw new RacingCommandException("Affichage des checkpoints déjà inactif !");
                        }
                    }
                    case "enable" -> {
                        if (!showCheckpoints.isEnabled()) {
                            player.sendMessage(ChatColor.GREEN + "Affichage des checkpoints activé !" + ChatColor.RESET);
                            showCheckpoints.enable();
                        } else {
                            throw new RacingCommandException("Affichage des checkpoints déjà actif !");
                        }
                    }
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public Map<String, TabExecutor> getSubcommands() {
        return new HashMap<>();
    }
}
