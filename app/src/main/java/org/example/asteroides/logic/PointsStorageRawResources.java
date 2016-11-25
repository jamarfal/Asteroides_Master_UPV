package org.example.asteroides.logic;

import android.content.Context;
import android.util.Log;

import org.example.asteroides.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by jamarfal on 9/11/16.
 */

public class PointsStorageRawResources implements PointsStorage {
    private Context context;

    public PointsStorageRawResources(Context context) {
        this.context = context;
    }

    @Override
    public void saveScore(int points, String name, long date) {
    }

    @Override
    public Vector<String> scoreList(int amount) {
        Vector<String> result = new Vector<String>();
        InputStream f = null;
        try {
            f = context.getResources().openRawResource(R.raw.puntuaciones);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(f));
            int n = 0;
            String linea;
            do {
                linea = entrada.readLine();
                if (linea != null) {
                    result.add(linea);
                    n++;
                }
            } while (n < amount && linea != null);

        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        } finally {
            try {
                if (f != null)
                    f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}