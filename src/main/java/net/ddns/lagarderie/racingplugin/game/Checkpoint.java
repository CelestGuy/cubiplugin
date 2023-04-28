package net.ddns.lagarderie.racingplugin.game;

import net.ddns.lagarderie.racingplugin.plugin.RacingGameException;
import org.bukkit.util.Vector;

import java.util.ArrayList;

import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.getCenteredPosition;

public class Checkpoint {
    private int id;
    private ArrayList<Integer> children;
    private float radius;
    private Vector position;
    private float yaw;
    private float pitch;

    public Checkpoint() {
        this.id = 0;
        this.position = new Vector();
        this.children = new ArrayList<>();
        this.radius = 1f;
        this.yaw = 0f;
        this.pitch = 0f;
    }

    public Checkpoint(int id, Vector position, float yaw, float pitch, float radius) {
        this.id = id;
        this.position = getCenteredPosition(position);
        this.children = new ArrayList<>();
        this.yaw = yaw;
        this.pitch = pitch;
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Integer> children) {
        this.children = children;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = getCenteredPosition(position);
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
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
