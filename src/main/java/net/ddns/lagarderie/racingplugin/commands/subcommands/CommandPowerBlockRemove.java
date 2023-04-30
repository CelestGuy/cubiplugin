package net.ddns.lagarderie.racingplugin.commands.subcommands;

import net.ddns.lagarderie.racingplugin.RacingPlugin;
import net.ddns.lagarderie.racingplugin.game.PowerBlock;
import net.ddns.lagarderie.racingplugin.game.PowerBlocksManager;
import net.ddns.lagarderie.racingplugin.plugin.RacingCommandException;
import net.ddns.lagarderie.racingplugin.plugin.RacingGameException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandPowerBlockRemove implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length == 1) {
                int val = Integer.parseInt(strings[0]);

                PowerBlocksManager manager = PowerBlocksManager.getInstance();

                if (manager.getPowerBlocks().isEmpty()) {
                    throw new RacingCommandException("Il n'existe aucun PowerBlocks.");
                } else if (val < 0) {
                    throw new RacingCommandException("L'argument doit être positif.");
                }

                if (manager.removePowerBlock(val)) {
                    player.sendMessage("PowerBlock " + val + ChatColor.RED + " supprimé" + ChatColor.RESET + " !");
                } else {
                    throw new RacingCommandException("Erreur lors de la suppression du PowerBlock");
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}
