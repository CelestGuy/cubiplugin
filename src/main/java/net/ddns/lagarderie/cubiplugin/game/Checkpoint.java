package net.ddns.lagarderie.cubiplugin.game;

import java.util.HashSet;
import java.util.Set;

public class Checkpoint {
    private TrackLocation trackLocation;
    private Set<Integer> childCheckpoints;
    private float radius;

    public Checkpoint() {
        trackLocation = null;
        childCheckpoints = new HashSet<>();
        radius = 1f;
    }

    public TrackLocation getTrackLocation() {
        return trackLocation;
    }

    public void setTrackLocation(TrackLocation trackLocation) {
        this.trackLocation = trackLocation;
    }

    public Set<Integer> getChildCheckpoints() {
        return childCheckpoints;
    }

    public void setChildCheckpoints(Set<Integer> childCheckpoints) {
        this.childCheckpoints = childCheckpoints;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Checkpoint (" +
                "§4" + trackLocation.getX() +
                "§r/§2" + trackLocation.getY() +
                "§r/§9" + trackLocation.getZ() +
                "§r) de rayon §c" + radius +
                "§r, prochains checkpoints : " + childCheckpoints.toString();
    }

    public void addChildCheckpoint(int value) {
        this.childCheckpoints.add(value);
    }

    public void removeChildCheckpoint(int value) {
        Integer val = value;
        this.childCheckpoints.remove(val);
    }
}
