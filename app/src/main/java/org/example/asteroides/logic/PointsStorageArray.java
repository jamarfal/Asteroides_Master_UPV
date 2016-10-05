package org.example.asteroides.logic;

import java.util.Vector;

/**
 * Created by jamarfal on 4/10/16.
 */

public class PointsStorageArray implements PointsStorage {

    private Vector<String> scores;

    public PointsStorageArray() {
        this.scores = new Vector<String>();
        addDumpData();
    }

    private void addDumpData() {
        this.scores.add("123000 Pepito Domingez");
        this.scores.add("111000 Pedro Martinez");
        this.scores.add("011000 Paco Pérez");
    }

    @Override
    public void saveScore(int points, String name, long date) {
        scores.add(0, points + " " + name);
    }

    @Override
    public Vector<String> scoreList(int amount) {
        return scores;
    }
}
