package org.example.asteroides.logic;

import android.content.Context;
import android.content.SharedPreferences;

import org.example.asteroides.logic.PointsStorage;

import java.util.Vector;

/**
 * Created by jamarfal on 9/11/16.
 */

public class AlmacenPuntuacionesPreferencias implements PointsStorage {

    private static String PREFERENCIAS = "puntuaciones";
    private Context context;

    public AlmacenPuntuacionesPreferencias(Context context) {
        this.context = context;
    }

    @Override
    public void saveScore(int puntos, String nombre, long fecha) {
        SharedPreferences preferencias = context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        for (int n = 9; n >= 1; n--) {
            editor.putString("puntuacion" + n,
                    preferencias.getString("puntuacion" + (n - 1), ""));
            editor.putString("puntuacion0", puntos + " " + nombre);
            editor.commit();
        }
    }

    @Override
    public Vector<String> scoreList(int cantidad) {
        Vector<String> result = new Vector<String>();
        SharedPreferences preferencias = context.getSharedPreferences(
                PREFERENCIAS, Context.MODE_PRIVATE);
        for (int n = 0; n <= 9; n++) {
            String s = preferencias.getString("puntuacion" + n, "");
            if (!s.isEmpty()) {
                result.add(s);
            }
        }
        return result;
    }

}