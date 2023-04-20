package net.ddns.lagarderie.cubiplugin.game;

import java.util.ArrayList;

public class Checkpoint {
    private boolean isIdSet;
    private int id;
    private TrackLocation trackLocation;
    private ArrayList<Integer> children;
    private float radius;

    public Checkpoint() {
        id = 0;
        trackLocation = null;
        children = new ArrayList<>();
        radius = 1f;
        isIdSet = false;
    }

    public void setId(int id) {
        if (!isIdSet) {
            this.id = id;
            isIdSet = true;
        }
    }

    public int getId() {
        return id;
    }

    public TrackLocation getTrackLocation() {
        return trackLocation;
    }

    public void setTrackLocation(TrackLocation trackLocation) {
        this.trackLocation = trackLocation;
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
        return "Checkpoint " + id + " (" +
                "§4" + trackLocation.getX() +
                "§r/§2" + trackLocation.getY() +
                "§r/§9" + trackLocation.getZ() +
                "§r) de rayon §c" + radius +
                "§r, checkpoints enfants : " + children.toString();
    }

    public void addChildCheckpoint(int value) {
        this.children.add(value);
    }

    public void removeChildCheckpoint(int value) {
        Integer val = value;
        this.children.remove(val);
    }
}
