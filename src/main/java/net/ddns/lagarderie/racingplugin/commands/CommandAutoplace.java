package net.ddns.lagarderie.racingplugin.commands;

import net.ddns.lagarderie.racingplugin.plugin.RacingCommandException;
import net.ddns.lagarderie.racingplugin.plugin.SafeCommandExecutor;
import net.ddns.lagarderie.racingplugin.tools.Autoplace;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandAutoplace extends SafeCommandExecutor {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            return List.of("enable", "disable", "defaultRadius");
        }

        return List.of();
    }

    @Override
    public boolean executeSafeCommand(CommandSender commandSender, Command command, String s, String[] strings) throws RacingCommandException {
        if (commandSender instanceof Player player) {
            Autoplace autoplace = Autoplace.getPlayer(player.getUniqueId());

            if (strings.length == 1) {
                switch (strings[0]) {
                    case "defaultRadius" -> player.sendMessage("Taille de rayon par défaut : " + autoplace.getDefaultCheckpointRadius());
                    case "disable" -> {
                        if (autoplace.isEnabled()) {
                            player.sendMessage(ChatColor.YELLOW + "Placement automatique des checkpoints désactivé !" + ChatColor.RESET);
                            Autoplace.removePlayer(player.getUniqueId());
                        } else {
                            throw new RacingCommandException("Placement automatique des checkpoints déjà inactif !");
                        }
                    }
                    case "enable" -> {
                        if (!autoplace.isEnabled()) {
                            player.sendMessage(ChatColor.GREEN + "Placement automatique des checkpoints activé !" + ChatColor.RESET);
                            autoplace.enable();
                        } else {
                            throw new RacingCommandException("Placement automatique des checkpoints déjà actif !");
                        }
                    }
                }

                return true;
            } else if (strings.length == 2) {
                boolean rad = strings[0].equals("defaultRadius");
                int radius = 0;
                try {
                    radius = Integer.parseInt(strings[1]);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("L'argument n'était pas un nombre");
                }

                if (rad) {
                    autoplace.setDefaultCheckpointRadius(radius);
                }
            }
        }

        return false;
    }

    @Override
    public Map<String, TabExecutor> getSubcommands() {
        return new HashMap<>();
    }
}
