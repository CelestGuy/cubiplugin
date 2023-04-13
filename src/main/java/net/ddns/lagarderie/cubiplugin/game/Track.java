package net.ddns.lagarderie.cubiplugin.game;

import java.util.ArrayList;
import java.util.List;

public class Track {
    private String mapId;
    private String name;
    private TrackLocation departureLineStart;
    private TrackLocation departureLineEnd;
    private final List<TrackLocation> checkpoints;

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

    public TrackLocation getDepartureLineStart() {
        return departureLineStart;
    }

    public void setDepartureLineStart(TrackLocation departureLineStart) {
        this.departureLineStart = departureLineStart;
    }

    public TrackLocation getDepartureLineEnd() {
        return departureLineEnd;
    }

    public void setDepartureLineEnd(TrackLocation departureLineEnd) {
        this.departureLineEnd = departureLineEnd;
    }

    public List<TrackLocation> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<TrackLocation> checkpoints) {
        if (checkpoints != null) {
            this.checkpoints.addAll(checkpoints);
        }
    }
}
