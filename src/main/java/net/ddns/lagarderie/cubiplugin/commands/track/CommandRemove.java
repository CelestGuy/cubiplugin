package net.ddns.lagarderie.cubiplugin.commands.track;

import net.ddns.lagarderie.cubiplugin.RacingPlugin;
import net.ddns.lagarderie.cubiplugin.game.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandRemove implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            for (Track track : RacingPlugin.getInstance().getTracks()) {
                if (track.getMapId().equals(player.getWorld().getName())) {
                    if (RacingPlugin.getInstance().getTracks().remove(track)) {
                        player.sendMessage("La course a été supprimée");
                        return true;
                    } else {
                        player.sendMessage("Une erreur est survenue lors de la suppression de la course");
                    }
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
