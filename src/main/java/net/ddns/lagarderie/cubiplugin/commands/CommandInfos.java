package net.ddns.lagarderie.cubiplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class CommandInfos implements CommandExecutor {
    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String s, String[] strings) {
        if (commandSender instanceof Player p) {
            p.sendMessage("Le plugin de la Garderie");

            return true;
        }

        return false;
    }
}
