package net.ddns.lagarderie.cubiplugin.commands;

import net.ddns.lagarderie.cubiplugin.RacingPlugin;
import net.ddns.lagarderie.cubiplugin.game.Track;
import net.ddns.lagarderie.cubiplugin.game.TrackLocation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandCheckpoint implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length == 1) {
                World playerWorld = player.getWorld();

                for (Track track : RacingPlugin.getInstance().getTracks()) {
                    if (track.getMapId().equals(playerWorld.getName())) {
                        switch (strings[0]) {
                            case "add" -> {
                                Location location = player.getLocation();
                                location.setX((int) (location.getX()) + 0.5);
                                location.setY((int) (location.getY()));
                                location.setZ((int) (location.getZ()) + 0.5);

                                track.getCheckpoints().add(new TrackLocation(location));
                                player.sendMessage("Checkpoint " +
                                        " - x: " + location.getX() +
                                        ", y: " + location.getY() +
                                        ", z: " + location.getZ() + " - Ajouté !"
                                );
                            }
                            case "remove" -> {
                                Location location = player.getLocation();
                                location.setX((int) (location.getX()) + 0.5);
                                location.setY((int) (location.getY()));
                                location.setZ((int) (location.getZ()) + 0.5);

                                for (TrackLocation tl : track.getCheckpoints()) {
                                    if ((int) (tl.getX()) == (int) (location.getX())
                                        && (int) (tl.getY()) == (int) (location.getY())
                                        && (int) (tl.getZ()) == (int) (location.getZ())) {
                                        if (track.getCheckpoints().remove(tl)) {
                                            player.sendMessage("Checkpoint supprimé !");
                                        }
                                    }
                                }
                            }
                            case "list" -> {
                                int i = 1;
                                for (TrackLocation location : track.getCheckpoints()) {
                                    player.sendMessage("Checkpoint n" + i +
                                            " - x: " + location.getX() +
                                            ", y: " + location.getY() +
                                            ", z: " + location.getZ()
                                    );

                                    i++;
                                }
                            }
                        }

                        return true;
                    }
                }

                player.sendMessage("§cThis world is not a track !");
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            return List.of("add", "remove", "list");
        }

        return List.of("");
    }
}
