package net.ddns.lagarderie.cubiplugin.commands.subcommands;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import net.ddns.lagarderie.cubiplugin.game.Racing;
import net.ddns.lagarderie.cubiplugin.game.Track;
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
                player.sendMessage("Nombre de tours: §a" + Racing.getInstance().getLapCount() + "§r");
            } else if (strings.length == 1) {
                String arg = strings[0];
                try {
                    int lap = Integer.parseInt(arg);
                    if (lap >= 0) {
                        Racing game = Racing.getInstance();
                        Track track = game.getTrack();

                        if (game.isRunning()) {
                            throw new RacingCommandException("Impossible de modifier les paramètres du jeu.");
                        } else if (track == null) {
                            throw new RacingCommandException("La course doit être choisie.");
                        } else {
                            if (track.getDepartureCheckpoint() == track.getArrivalCheckpoint()) {
                                game.setLapCount(lap);

                                player.sendMessage("Nombre de tours mis à §d" + lap + "§r !");
                                return true;
                            } else {
                                throw new RacingCommandException("La course ne forme pas une boucle.");
                            }
                        }
                    } else {
                        throw new RacingCommandException("L'argument spécifié doit être supérieur à 0.");
                    }
                } catch (NumberFormatException e) {
                    throw new RacingCommandException("L'argument spécifié dans la commande n'était pas un nombre.");
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
