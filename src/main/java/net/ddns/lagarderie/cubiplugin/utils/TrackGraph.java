package net.ddns.lagarderie.cubiplugin.utils;

import net.ddns.lagarderie.cubiplugin.game.Checkpoint;
import net.ddns.lagarderie.cubiplugin.game.Track;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class TrackGraph {
    public static float getDistance(Checkpoint a, Checkpoint b) {
        Vector vecA = a.getLocation().toVector();
        Vector vecB = b.getLocation().toVector();

        return (float) (vecA.clone().subtract(vecB).length());
    }

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

            for (Checkpoint child : getChildren(track, c)) {
                float distance = distances.get(cId) + getDistance(c, child);
                int childId = child.getId();

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

    private static ArrayList<Checkpoint> getChildren(Track track, Checkpoint c) {
        ArrayList<Checkpoint> children = new ArrayList<>();

        for (Integer childId : c.getChildren()) {
            children.add(track.getCheckpoint(childId));
        }

        return children;
    }
}
