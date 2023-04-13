package net.ddns.lagarderie.cubiplugin.commands.track;

import net.ddns.lagarderie.cubiplugin.RacingPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandTrackDebug implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length == 1) {
                boolean enable = strings[0].equals("enable");

                if (enable) {
                    RacingPlugin.getInstance().getTrackDebugMode().start();
                    player.sendMessage("§eDebug activé !");
                } else {
                    RacingPlugin.getInstance().getTrackDebugMode().stop();
                    player.sendMessage("§cDebug désactivé !");
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            return List.of("enable", "disable");
        }

        return List.of();
    }
}
