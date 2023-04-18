package net.ddns.lagarderie.cubiplugin.commands.checkpoint;

import net.ddns.lagarderie.cubiplugin.RacingPlugin;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import net.ddns.lagarderie.cubiplugin.game.Checkpoint;
import net.ddns.lagarderie.cubiplugin.game.Track;
import net.ddns.lagarderie.cubiplugin.modes.CheckpointDrawingMode;
import net.ddns.lagarderie.cubiplugin.modes.RacingMode;
import net.ddns.lagarderie.cubiplugin.modes.TrackDebugMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static net.ddns.lagarderie.cubiplugin.utils.TrackUtils.getTrack;

public class CommandCheckpointDraw implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            RacingPlugin racingPlugin = RacingPlugin.getInstance();
            RacingMode cdm = racingPlugin.getMode(player.getName());

            if (strings.length == 1) {
                boolean enable = strings[0].equals("enable");

                if (!(cdm instanceof CheckpointDrawingMode)) {
                    try {
                        cdm = new CheckpointDrawingMode(player, getTrack(player.getWorld().getName()));
                    } catch (RacingGameException e) {
                        throw new RacingCommandException(e.getMessage());
                    }
                }

                if (enable) {
                    if (racingPlugin.startMode(player.getName(), cdm)) {
                        player.sendMessage("§ePlacement automatique des checkpoints activé !");
                    }
                } else {
                    switch (strings[0]) {
                        case "defaultRadius" -> player.sendMessage("Valeur par défaut : " + ((CheckpointDrawingMode) cdm).getDefaultCheckpointRadius());
                        case "disable" -> {
                            if (racingPlugin.stopMode(player.getName(), cdm)) {
                                player.sendMessage("§cPlacement automatique des checkpoints désactivé !");
                            }
                        }
                    }
                }

                return true;
            } else if (strings.length == 2) {
                boolean rad = strings[0].equals("defaultRadius");

                if (rad && cdm instanceof CheckpointDrawingMode c) {
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
