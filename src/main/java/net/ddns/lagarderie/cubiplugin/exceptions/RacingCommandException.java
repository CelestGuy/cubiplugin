package net.ddns.lagarderie.cubiplugin.exceptions;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;

public class RacingCommandException extends CommandException {
    public RacingCommandException(String message) {
        super(ChatColor.RED + message + ChatColor.RESET);
    }
}
