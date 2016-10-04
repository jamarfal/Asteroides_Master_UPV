package org.example.asteroides.logic;

import java.util.Vector;

/**
 * Created by jamarfal on 4/10/16.
 */

public interface PointsStorage {

    public void saveScore(int points, String name, long date);

    public Vector<String> scoreList(int amount);
}
