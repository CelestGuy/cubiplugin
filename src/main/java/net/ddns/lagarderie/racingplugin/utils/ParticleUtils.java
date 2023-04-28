package net.ddns.lagarderie.racingplugin.utils;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParticleUtils {
    public static void drawCircle(Player player, Vector position, Color color, float radius) {
        float dx = 0;
        float dy = (radius * 2f);
        float d = 3f - (2f * (radius * 2f));

        do {
            drawCircle(player, position, color, dx, dy);
            drawCircle(player, position, color, dy, dx);

            dx++;

            if (d > 0) {
                dy--;
                d += 4f * (dx - dy) + 10f;
            } else {
                d += 4f * dx + 6f;
            }
        } while (dy >= dx);
    }

    public static void drawCircle(Player player, Vector position, Color color, float dx, float dy) {
        float ddx = dx * .5f;
        float ddy = dy * .5f;

        spawnRedstoneParticle(player, position.clone().add(new Vector(ddx, 0, ddy)), color, 0.1);
        spawnRedstoneParticle(player, position.clone().add(new Vector(-ddx, 0, ddy)), color, 0.1);
        spawnRedstoneParticle(player, position.clone().add(new Vector(ddx, 0, -ddy)), color, 0.1);
        spawnRedstoneParticle(player, position.clone().add(new Vector(-ddx, 0, -ddy)), color, 0.1);
    }

    public static void spawnRedstoneParticle(Player player, Vector position, Color color, double deltaY) {
        Particle.DustOptions options = new Particle.DustOptions(color, 1f);

        player.spawnParticle(
                Particle.REDSTONE,
                position.getX(),
                position.getY(),
                position.getZ(),
                1,
                0,
                deltaY,
                0,
                options
        );
    }
}
