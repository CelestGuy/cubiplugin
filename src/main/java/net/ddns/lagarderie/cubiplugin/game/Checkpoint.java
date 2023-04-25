package net.ddns.lagarderie.cubiplugin.game;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;
import org.bukkit.Location;

import java.util.ArrayList;

public class Checkpoint {
    private boolean idSet;
    private int id;
    private Location location;
    private ArrayList<Integer> children;
    private float radius;

    public Checkpoint() {
        id = 0;
        location = null;
        children = new ArrayList<>();
        radius = 1f;
        idSet = false;
    }

    public void setId(int id) {
        if (!idSet) {
            this.id = id;
            idSet = true;
        }
    }

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<Integer> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Integer> children) {
        this.children = children;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Id " + id + " (" +
                "§4" + location.getX() +
                "§r/§2" + location.getY() +
                "§r/§9" + location.getZ() +
                "§r) de rayon §c" + radius +
                "§r, checkpoints enfants : " + children.toString();
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
}
