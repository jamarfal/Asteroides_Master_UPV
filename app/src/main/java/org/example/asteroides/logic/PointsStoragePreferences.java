package org.example.asteroides.logic;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Vector;

/**
 * Created by jamarfal on 9/11/16.
 */

public class PointsStoragePreferences extends PointsStorageBase {

    private static String PREFERENCES = "puntuaciones";


    public PointsStoragePreferences(Context context) {
        super(context);

    }

    @Override
    public void saveScore(int points, String name, long date) {
        SharedPreferences preferencias = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        for (int n = 9; n >= 1; n--) {
            editor.putString("puntuacion" + n,
                    preferencias.getString("puntuacion" + (n - 1), ""));
        }
        editor.putString("puntuacion0", points + " " + name);
        editor.apply();
    }

    @Override
    public Vector<String> scoreList(int amount) {
        Vector<String> result = new Vector<String>();
        SharedPreferences preferencias = context.getSharedPreferences(
                PREFERENCES, Context.MODE_PRIVATE);
        for (int n = 0; n <= 9; n++) {
            String s = preferencias.getString("puntuacion" + n, "");
            if (!s.isEmpty()) {
                result.add(s);
            }
        }
        return result;
    }

}