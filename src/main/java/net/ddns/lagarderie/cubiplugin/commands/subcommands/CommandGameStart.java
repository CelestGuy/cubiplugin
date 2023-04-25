package net.ddns.lagarderie.cubiplugin.commands.subcommands;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingCommandException;
import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import net.ddns.lagarderie.cubiplugin.game.Racing;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class CommandGameStart implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        try {
            Racing game = Racing.getInstance();

            if (game.isRunning()) {
                throw new RacingCommandException("");
            } else {
                try {
                    Racing.getInstance().start();
                } catch (RacingGameException e) {
                    throw new RacingCommandException(e.getMessage());
                }
            }

            return true;
        } catch (Exception e) {
            throw new RacingCommandException(e.getMessage());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}
