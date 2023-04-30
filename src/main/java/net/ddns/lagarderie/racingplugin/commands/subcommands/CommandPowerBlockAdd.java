package net.ddns.lagarderie.racingplugin.commands.subcommands;

import net.ddns.lagarderie.racingplugin.RacingPlugin;
import net.ddns.lagarderie.racingplugin.game.PowerBlock;
import net.ddns.lagarderie.racingplugin.game.PowerBlocksManager;
import net.ddns.lagarderie.racingplugin.game.RacingGame;
import net.ddns.lagarderie.racingplugin.plugin.RacingCommandException;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class CommandPowerBlockAdd implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length == 5) {
                String blockId = strings[0].toUpperCase();
                String effectId = strings[1].toUpperCase();

                Material block = Material.getMaterial(blockId);
                PotionEffectType effectType = PotionEffectType.getByName(effectId);
                int effectDuration = Integer.parseInt(strings[2]);
                int effectAmplifier = Integer.parseInt(strings[3]);
                boolean speedAdaptative = Boolean.parseBoolean(strings[4]);

                if (block == null) {
                    throw new RacingCommandException("Le bloc n'existe pas");
                } else if (effectType == null) {
                    throw new RacingCommandException("L'effet de potion n'existe pas");
                } else if (effectDuration <= 0) {
                    throw new RacingCommandException("La durée de l'effet doit être supérieur à 0");
                } else if (effectAmplifier <= 0) {
                    throw new RacingCommandException("L'amplificateur de l'effet doit être supérieur à 0");
                } else {
                    PowerBlocksManager manager = PowerBlocksManager.getInstance();

                    PowerBlock pb = new PowerBlock(
                            manager.getMaxId() + 1,
                            block,
                            effectId,
                            effectDuration,
                            effectAmplifier,
                            speedAdaptative
                    );

                    if (manager.addPowerBlock(pb)) {
                        player.sendMessage(pb.toString() + ChatColor.GREEN + " ajouté" + ChatColor.RESET);
                    } else {
                        throw new RacingCommandException("Erreur dans l'ajout du powerblock");
                    }
                }

            } else {
                throw new RacingCommandException("Pas assez d'arguments");
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            ArrayList<String> args = new ArrayList<>();
            String arg = strings[0].toLowerCase();

            for (Material material : Material.values()) {
                String materialName = material.name().toLowerCase();
                if (materialName.contains(arg)) {
                    args.add(materialName);
                }
            }

            return args;
        } else if (strings.length == 2) {
            ArrayList<String> args = new ArrayList<>();
            String arg = strings[1].toLowerCase();

            for (PotionEffectType pet : PotionEffectType.values()) {
                String petName = pet.getName().toLowerCase();
                if (petName.contains(arg)) {
                    args.add(petName);
                }
            }

            return args;
        } else if (strings.length == 5) {
            return List.of("true", "false");
        }

        return List.of();
    }
}
