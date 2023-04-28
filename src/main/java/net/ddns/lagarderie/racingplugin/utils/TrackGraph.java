package net.ddns.lagarderie.racingplugin.utils;

import net.ddns.lagarderie.racingplugin.game.Checkpoint;
import net.ddns.lagarderie.racingplugin.game.Track;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import static net.ddns.lagarderie.racingplugin.utils.TrackUtils.getDistance;

public class TrackGraph {
    public static float getShortestDistance(Track track, Checkpoint a, Checkpoint b) {
        // TODO : Enhance this cringe code

        int aId = a.getId();
        int bId = b.getId();

        HashMap<Integer, Float> distances = new HashMap<>();
        HashSet<Integer> marks = new HashSet<>();
        LinkedList<Integer> file = new LinkedList<>();

        for (int i = 0; i < track.getCheckpoints().size(); i++) {
            distances.put(i, Float.MAX_VALUE);
        }

        file.add(aId);
        distances.replace(aId, 0f);

        while (!file.isEmpty()) {
            double min = Double.POSITIVE_INFINITY;
            int index = 0;
            for (int i = 0; i < file.size(); i++) {
                if (distances.get(file.get(i)) < min) {
                    min = distances.get(file.get(i));
                    index = i;
                }
            }
            int cId = file.remove(index);
            marks.add(cId);

            Checkpoint c = track.getCheckpoint(cId);

            for (Integer childId : c.getChildren()) {
                Checkpoint child = track.getCheckpoint(childId);
                float distance = distances.get(cId) + getDistance(c, child);

                if (childId == bId) {
                    distances.put(childId, distance);
                    return distances.get(childId);
                } else if (!file.contains(childId) && !marks.contains(childId)) {
                    distances.put(childId, distance);
                    file.add(childId);
                } else if (distance < distances.get(childId)) {
                    distances.replace(childId, distance);
                }
            }
        }

        return distances.get(bId);
    }
}
