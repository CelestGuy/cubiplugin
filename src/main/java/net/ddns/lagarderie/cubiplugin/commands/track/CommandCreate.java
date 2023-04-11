package net.ddns.lagarderie.cubiplugin.commands.track;

import net.ddns.lagarderie.cubiplugin.RacingPlugin;
import net.ddns.lagarderie.cubiplugin.game.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandCreate implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            Track track = new Track();
            track.setMapId(player.getWorld().getName());
            track.setName(player.getWorld().getName());

            if (RacingPlugin.getInstance().getTracks().add(track)) {
                player.sendMessage("La course a été créée");
                return true;
            } else {
                player.sendMessage("Une erreur est survenue lors de la création de la course");
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of("");
    }
}
