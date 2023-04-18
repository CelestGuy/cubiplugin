package net.ddns.lagarderie.cubiplugin.game;

public class Checkpoint {
    private TrackLocation trackLocation;
    private int value;
    private float radius;

    public Checkpoint() {
        trackLocation = null;
        value = 0;
        radius = 1f;
    }

    public TrackLocation getTrackLocation() {
        return trackLocation;
    }

    public void setTrackLocation(TrackLocation trackLocation) {
        this.trackLocation = trackLocation;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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
                "§r/§9" + trackLocation.getZ() + "§r) de valeur §c" + value + "§r et de rayon §c" + radius + "§r";
    }
}
