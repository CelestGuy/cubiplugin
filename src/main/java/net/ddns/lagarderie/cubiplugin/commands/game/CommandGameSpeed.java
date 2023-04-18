package net.ddns.lagarderie.cubiplugin.commands.game;

import net.ddns.lagarderie.cubiplugin.game.Racing;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandGameSpeed implements TabExecutor {
    private static final List<String> availableSpeeds = List.of("50", "100", "150", "200", "999");
    private static final List<String> speedColor = List.of("§a", "§2", "§6", "§c", "§5");

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length == 0) {
                int speed = Racing.getInstance().getSpeed();
                String color = speedColor.get(availableSpeeds.indexOf("" + speed));

                player.sendMessage("La vitesse actuelle de la course est " + color + speed + "§r !");
            }
            else if (strings.length == 1) {
                String arg = strings[0];
                if (availableSpeeds.contains(arg)) {
                    int speed = Integer.parseInt(arg);
                    Racing.getInstance().setSpeed(speed);

                    String color = speedColor.get(availableSpeeds.indexOf(arg));

                    player.sendMessage("La vitesse de la course a été mise à " + color + speed + "§r !");
                    return true;
                } else {
                    player.sendMessage("§cLa vitesse n'existe pas.§r");
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            return availableSpeeds;
        }

        return List.of();
    }
}