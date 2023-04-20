package net.ddns.lagarderie.cubiplugin.game;

import net.ddns.lagarderie.cubiplugin.exceptions.RacingGameException;

import java.util.ArrayList;
public class Track {
    private String mapId;
    private String name;
    private int departureCheckpoint;
    private int arrivalCheckpoint;
    private ArrayList<Checkpoint> checkpoints;

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

    public ArrayList<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(ArrayList<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public int getDepartureCheckpoint() {
        return departureCheckpoint;
    }

    public void setDepartureCheckpoint(int departureCheckpoint) {
        this.departureCheckpoint = departureCheckpoint;
    }

    public int getArrivalCheckpoint() {
        return arrivalCheckpoint;
    }

    public void setArrivalCheckpoint(int arrivalCheckpoint) {
        this.arrivalCheckpoint = arrivalCheckpoint;
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

    public Checkpoint getCheckpoint(Integer id) {
        for (Checkpoint c : checkpoints) {
            if (c.getId() == id) {
                return c;
            }
        }

        return null;
    }
}
