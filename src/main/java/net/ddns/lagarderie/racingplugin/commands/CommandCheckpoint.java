package net.ddns.lagarderie.racingplugin.commands;

import net.ddns.lagarderie.racingplugin.commands.subcommands.*;
import net.ddns.lagarderie.racingplugin.game.Checkpoint;
import net.ddns.lagarderie.racingplugin.game.Track;
import net.ddns.lagarderie.racingplugin.plugin.RacingCommandException;
import net.ddns.lagarderie.racingplugin.plugin.RacingGameException;
import net.ddns.lagarderie.racingplugin.plugin.SafeCommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.getClosestCheckpoint;
import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.getTrack;

public class CommandCheckpoint extends SafeCommandExecutor {
    @Override
    public boolean executeSafeCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0 && commandSender instanceof Player player) {
            String worldName = player.getWorld().getName();
            Track track;
            Checkpoint checkpoint;

            try {
                track = getTrack(worldName);
                checkpoint = getClosestCheckpoint(player, track);
            } catch (RacingGameException | NumberFormatException e) {
                throw new RacingCommandException(e.getMessage());
            }

            if (checkpoint != null) {
                player.sendMessage("Checkpoint le plus proche : " + checkpoint);
            }
        }

        return handleCommand(commandSender, command, s, strings);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return getStrings(commandSender, command, s, strings);
    }

    @Override
    public Map<String, TabExecutor> getSubcommands() {
        HashMap<String, TabExecutor> subcommands = new HashMap<>();

        subcommands.put("add", new CommandCheckpointAdd());
        subcommands.put("list", new CommandCheckpointList());
        subcommands.put("radius", new CommandCheckpointRadius());
        subcommands.put("remove", new CommandCheckpointRemove());
        subcommands.put("tp", new CommandCheckpointTeleport());

        return subcommands;
    }
}
