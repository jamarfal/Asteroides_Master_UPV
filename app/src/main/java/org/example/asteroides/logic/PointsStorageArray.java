package org.example.asteroides.logic;

import android.content.Context;

import org.example.asteroides.logic.storage_operations.PointsStorageBase;

import java.util.Vector;

/**
 * Created by jamarfal on 4/10/16.
 */

public class PointsStorageArray extends PointsStorageBase {

    private Vector<String> scores;

    public PointsStorageArray(Context context) {
        super(context);
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
