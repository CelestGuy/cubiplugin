package net.ddns.lagarderie.racingplugin.game;

import java.util.ArrayList;

public class Track {
    private String id;
    private String name;
    private int departureCheckpoint;
    private int arrivalCheckpoint;
    private ArrayList<Checkpoint> checkpoints;

    public Track() {
        checkpoints = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ArrayList<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(ArrayList<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public void addCheckpoint(Checkpoint checkpoint) {
        if (checkpoint != null) {
            checkpoints.add(checkpoint);
        }
    }

    public void removeCheckpoint(Checkpoint checkpoint) {
        if (checkpoint != null) {
            checkpoints.remove(checkpoint);
        }
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
