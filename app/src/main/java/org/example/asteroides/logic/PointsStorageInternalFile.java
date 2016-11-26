package org.example.asteroides.logic;

import android.content.Context;
import android.util.Log;

import org.example.asteroides.logic.storage_operations.PointsStorageBase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by jamarfal on 9/11/16.
 */

public class PointsStorageInternalFile extends PointsStorageBase {
    private static String FILE_NAME = "puntuaciones_internal.txt";

    public PointsStorageInternalFile(Context context) {
        super(context);
    }

    @Override
    public void saveScore(int points, String name, long date) {
        Log.i("Asteroides", "comienza salvado interno");
        FileOutputStream f = null;
        try {

            f = context.openFileOutput(FILE_NAME, Context.MODE_APPEND);
            String text = points + " " + name + "\n";
            f.write(text.getBytes());
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        } finally {
            if (f != null) try {
                Log.i("Asteroides", "Salvado interno completo");
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Vector<String> scoreList(int amount) {
        Vector<String> result = new Vector<String>();
        FileInputStream f = null;
        try {
            f = context.openFileInput(FILE_NAME);
            BufferedReader input = new BufferedReader(new InputStreamReader(f));
            int n = 0;
            String line;
            do {
                line = input.readLine();
                if (line != null) {
                    result.add(line);
                    n++;
                }
            } while (n < amount && line != null);

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