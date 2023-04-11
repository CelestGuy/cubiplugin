package net.ddns.lagarderie.cubiplugin.game;

import org.bukkit.Location;
import org.bukkit.World;

public class TrackLocation extends Location {
    public TrackLocation() {
        super(null, 0., 0., 0.);
    }

    public TrackLocation(Location location) {
        super(null, location.getX(), location.getY(), location.getZ());
    }

    public TrackLocation(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public TrackLocation(World world, double x, double y, double z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
    }
}
