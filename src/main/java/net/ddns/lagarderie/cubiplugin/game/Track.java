package net.ddns.lagarderie.cubiplugin.game;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;

import java.util.ArrayList;
import java.util.List;

public class Track {
    private String mapId;
    private String name;
    private List<Checkpoint> checkpoints;

    private int departureCheckpoint;
    private int arrivalCheckpoint;


    public Track() {
        checkpoints = new ArrayList<>();
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public void addCheckpoint(Checkpoint checkpoint) {
        if (checkpoint != null) {
            checkpoints.add(checkpoint);
        }
    }

    public boolean removeCheckpoint(Checkpoint checkpoint) {
        if (checkpoint != null) {
            return checkpoints.remove(checkpoint);
        }
        return false;
    }

    public Checkpoint removeCheckpoint(int index) throws RacingGameException {
        if (index < 0 || index >= checkpoints.size()) {
            throw new RacingGameException("L'index ne peut être négatif ou supérieur au nombre total de checkpoints");
        }
        return checkpoints.remove(index);
    }
}
