package net.ddns.lagarderie.cubiplugin.game;

import net.ddns.lagarderie.cubiplugin.RacingPlugin;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.ArrayList;

public class TrackDebugMode implements Runnable {
    private Thread debugThread;
    private boolean running;

    public void start() {
        debugThread = new Thread(this, "Debug Mode");
        debugThread.start();
        running = true;
    }

    @Override
    public void run() {
        while (running) {
            ArrayList<Track> tracks = RacingPlugin.getInstance().getTracks();

            for (World world : RacingPlugin.getInstance().getServer().getWorlds()) {
                for (Track track : tracks) {
                    if (track.getMapId().equals(world.getName())) {
                        for (TrackLocation checkpoint : track.getCheckpoints()) {
                            world.spawnParticle(Particle.CLOUD, checkpoint, 1);
                        }
                    }
                }
            }

            try {
                Thread.sleep(64);
            } catch (InterruptedException e) {
                debugThread.interrupt();
                throw new RuntimeException(e);
            }
        }
    }

    public void stop() {
        running = false;
    }
}
