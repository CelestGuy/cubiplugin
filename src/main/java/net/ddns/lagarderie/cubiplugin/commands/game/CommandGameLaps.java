package net.ddns.lagarderie.cubiplugin.commands.game;

import net.ddns.lagarderie.cubiplugin.game.Racing;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandGameLaps implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length == 0) {
                player.sendMessage("La nombre actuel de tours de la course est de §a" + Racing.getInstance().getLapCount() + "§r !");
            } else if (strings.length == 1) {
                String arg = strings[0];
                try {
                    int lap = Integer.parseInt(arg);
                    if (lap >= 0) {
                        Racing.getInstance().setLapCount(lap);

                        player.sendMessage("La nombre de tours dans la course a été mis à §d" + lap + "§r !");
                        return true;
                    } else {
                        player.sendMessage("§cL'argument spécifié doit être supérieur à 0 !");
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage("§cL'argument spécifié dans la commande n'était pas un nombre.");
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}
