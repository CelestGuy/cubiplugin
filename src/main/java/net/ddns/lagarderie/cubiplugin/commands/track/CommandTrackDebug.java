package net.ddns.lagarderie.cubiplugin.commands.track;

import net.ddns.lagarderie.cubiplugin.RacingPlugin;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import net.ddns.lagarderie.cubiplugin.modes.RacingMode;
import net.ddns.lagarderie.cubiplugin.modes.TrackDebugMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static net.ddns.lagarderie.cubiplugin.utils.TrackUtils.getTrack;

public class CommandTrackDebug implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length == 1) {
                boolean enable = strings[0].equals("enable");
                RacingPlugin racingPlugin = RacingPlugin.getInstance();
                RacingMode tdm = racingPlugin.getMode(player.getName());

                if (!(tdm instanceof TrackDebugMode)) {
                    try {
                        tdm = new TrackDebugMode(player, getTrack(player.getWorld().getName()));
                    } catch (RacingGameException e) {
                        throw new RacingCommandException(e.getMessage());
                    }
                }

                if (enable) {
                    if (racingPlugin.startMode(player.getName(), tdm)) {
                        player.sendMessage("§eDebug activé !");
                    }
                } else {
                    if (racingPlugin.stopMode(player.getName(), tdm)) {
                        player.sendMessage("§cDebug désactivé !");
                    }
                }

                return true;
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
