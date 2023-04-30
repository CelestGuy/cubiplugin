package net.ddns.lagarderie.racingplugin.game;

import net.ddns.lagarderie.racingplugin.plugin.RacingGameException;
import org.bukkit.util.Vector;

import java.util.ArrayList;

import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.getCenteredPosition;

public class Checkpoint {
    private final int id;
    private final ArrayList<Integer> children;
    private CheckpointType type;
    private double angle;
    private double radius;
    private Vector position;
    private double yaw;
    private double pitch;

    public Checkpoint(int id, CheckpointType type, Vector position, double yaw, double pitch, double radius, double angle) {
        this.id = id;
        this.type = type;
        this.position = getCenteredPosition(position);
        this.children = new ArrayList<>();
        this.yaw = yaw;
        this.pitch = pitch;
        this.radius = radius;
        this.angle = angle;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getChildren() {
        return children;
    }

    public CheckpointType getType() {
        return type;
    }

    public void setType(CheckpointType type) {
        this.type = type;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = getCenteredPosition(position);
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = Math.toRadians(angle);
    }

    public void addChildCheckpoint(int childId) throws RacingGameException {
        if (childId < 0) {
            throw new RacingGameException("L'id d'un checkpoint ne peut être négatif.");
        }

        Integer cId = childId;
        this.children.add(cId);
    }

    public void removeChildCheckpoint(int childId) throws RacingGameException {
        if (childId < 0) {
            throw new RacingGameException("L'id d'un checkpoint ne peut être négatif.");
        }

        Integer cId = childId;
        this.children.remove(cId);
    }

    @Override
    public String toString() {
        return "Id " + id + " (" + "§4" + position.getX() + "§r/§2" + position.getY() + "§r/§9" + position.getZ() + "§r) de rayon §c" + radius + "§r, successeurs : " + children.toString();
    }
}
