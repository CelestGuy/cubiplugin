package net.ddns.lagarderie.cubiplugin.commands.subcommands;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import net.ddns.lagarderie.cubiplugin.game.Racing;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class CommandGameStop implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        try {
            Racing game = Racing.getInstance();

            if (!game.isRunning()) {
                throw new RacingCommandException("Un jeu n'est pas en cours.");
            } else {
                game.stop();
                commandSender.sendMessage("Jeu arrêté.");
                return true;
            }
        } catch (Exception e) {
            throw new RacingCommandException(e.getMessage());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}
