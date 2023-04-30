package net.ddns.lagarderie.racingplugin.utils;

import net.ddns.lagarderie.racingplugin.game.Checkpoint;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParticleUtils {
    public static void drawCheckpoint(Player player, Checkpoint checkpoint) {
        Vector pos = checkpoint.getPosition();
        spawnRedstoneParticle(player, pos, Color.YELLOW, 0.5f);

        switch (checkpoint.getType()) {
            case CIRCLE -> {
                bresenhamCircle(player, pos, Color.TEAL, checkpoint.getRadius());
            }
            case RECTANGLE -> {
                double width = checkpoint.getRadius() / 2D;

                Vector a = new Vector(pos.getX() - width, pos.getY(), pos.getZ() - 0.5).rotateAroundAxis(pos, checkpoint.getAngle());
                Vector b = new Vector(pos.getX() + width, pos.getY(), pos.getZ() - 0.5).rotateAroundAxis(pos, checkpoint.getAngle());
                Vector c = new Vector(pos.getX() + width, pos.getY(), pos.getZ() + 0.5).rotateAroundAxis(pos, checkpoint.getAngle());
                Vector d = new Vector(pos.getX() - width, pos.getY(), pos.getZ() + 0.5).rotateAroundAxis(pos, checkpoint.getAngle());

                bresenhamLine(player, a, b, Color.TEAL);
                bresenhamLine(player, b, c, Color.TEAL);
                bresenhamLine(player, c, d, Color.TEAL);
                bresenhamLine(player, d, a, Color.TEAL);
            }
        }
    }

    public static void bresenhamLine(Player player, Vector a, Vector b, Color color) {
        float xA = (float) a.getX();
        float zA = (float) a.getZ();
        float xB = (float) b.getX();
        float zB = (float) b.getZ();

        float dx = Math.abs(xB - xA);
        float dz = Math.abs(zB - zA);

        if (dx < dz) {
            float tempA = xA;
            xA = zA;
            zA = tempA;

            float tempB = xB;
            xB = zB;
            zB = tempB;

            float tempD = dx;
            dx = dz;
            dz = tempD;
        }

        float d = 2 * dz - dx;

        for (int i = 0; i <= dx; i++) {
            Vector pos = new Vector(xA, a.getY(), zA);

            spawnRedstoneParticle(player, pos, Color.TEAL, 0.1);

            if (xA < xB) {
                xA++;
            } else {
                xB--;
            }

            if (d < 0) {
                d += 2 * dz;
            } else {
                if (zA < zB) {
                    zA++;
                } else {
                    zA--;
                }

                d += 2 * dz - 2 * dx;
            }
        }
    }

    public static void bresenhamCircle(Player player, Vector position, Color color, double radius) {
        double dx = 0;
        double dy = (radius * 2D);
        double d = 3D - (2D * (radius * 2D));

        do {
            drawCircle(player, position, color, dx, dy);
            drawCircle(player, position, color, dy, dx);

            dx++;

            if (d > 0D) {
                dy--;
                d += 4D * (dx - dy) + 10D;
            } else {
                d += 4D * dx + 6D;
            }
        } while (dy >= dx);
    }

    public static void drawCircle(Player player, Vector position, Color color, double dx, double dy) {
        double ddx = dx * .5D;
        double ddy = dy * .5D;

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
