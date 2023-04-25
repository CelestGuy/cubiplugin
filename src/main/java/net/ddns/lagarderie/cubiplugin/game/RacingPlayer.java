package net.ddns.lagarderie.cubiplugin.game;

import java.util.UUID;

public class RacingPlayer implements Comparable<RacingPlayer> {
    private final UUID uuid;
    private float distance;
    private int checkedCheckpoints;
    private int checkedLaps;

    public RacingPlayer(UUID uuid) {
        this.uuid = uuid;
        this.distance = 0f;
    }

    public UUID getUuid() {
        return uuid;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getCheckedCheckpoints() {
        return checkedCheckpoints;
    }

    public void setCheckedCheckpoints(int checkedCheckpoints) {
        this.checkedCheckpoints = checkedCheckpoints;
    }

    public int getCheckedLaps() {
        return checkedLaps;
    }

    public void setCheckedLaps(int checkedLaps) {
        this.checkedLaps = checkedLaps;
    }

    @Override
    public int compareTo(RacingPlayer o) {
        int i = 0;

        if (distance < o.distance) {
            i = -1;
        } else if (distance > o.distance) {
            i = 1;
        }

        return i;
    }
}
