package net.ddns.lagarderie.cubiplugin.game;

import java.util.UUID;

public class PlayerDistance implements Comparable<PlayerDistance> {
    private final UUID uuid;
    private final float distance;

    public PlayerDistance(UUID uuid, float distance) {
        this.uuid = uuid;
        this.distance = distance;
    }

    public UUID getUuid() {
        return uuid;
    }

    public float getDistance() {
        return distance;
    }

    @Override
    public int compareTo(PlayerDistance o) {
        int i = 0;

        if (distance < o.distance) {
            i = -1;
        } else if (distance > o.distance) {
            i = 1;
        }

        return i;
    }
}
