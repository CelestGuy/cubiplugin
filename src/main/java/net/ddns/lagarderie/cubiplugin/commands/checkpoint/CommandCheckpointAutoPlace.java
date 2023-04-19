package net.ddns.lagarderie.cubiplugin.commands.checkpoint;

import net.ddns.lagarderie.cubiplugin.RacingPlugin;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import net.ddns.lagarderie.cubiplugin.modes.CheckpointAutoPlaceMode;
import net.ddns.lagarderie.cubiplugin.modes.RacingMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static net.ddns.lagarderie.cubiplugin.utils.TrackUtils.getTrack;

public class CommandCheckpointAutoPlace implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            RacingPlugin racingPlugin = RacingPlugin.getInstance();
            RacingMode cdm = racingPlugin.getMode(player.getName());

            if (strings.length == 1) {
                if (!(cdm instanceof CheckpointAutoPlaceMode)) {
                    try {
                        cdm = new CheckpointAutoPlaceMode(player, getTrack(player.getWorld().getName()));
                    } catch (RacingGameException e) {
                        throw new RacingCommandException(e.getMessage());
                    }
                }

                switch (strings[0]) {
                    case "defaultRadius" -> player.sendMessage("Taille de rayon par défaut : " + ((CheckpointAutoPlaceMode) cdm).getDefaultCheckpointRadius());
                    case "disable" -> {
                        if (racingPlugin.stopMode(player.getName(), cdm)) {
                            player.sendMessage("§cPlacement automatique des checkpoints désactivé !");
                        }
                    }
                    case "enable" -> {
                        if (racingPlugin.startMode(player.getName(), cdm)) {
                            player.sendMessage("§ePlacement automatique des checkpoints activé !");
                        }
                    }
                }

                return true;
            } else if (strings.length == 2) {
                boolean rad = strings[0].equals("defaultRadius");

                if (rad && cdm instanceof CheckpointAutoPlaceMode c) {
                    c.setDefaultCheckpointRadius(Integer.parseInt(strings[1]));
                }
            }

        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            return List.of("enable", "disable", "defaultRadius");
        }

        return List.of();
    }
}
