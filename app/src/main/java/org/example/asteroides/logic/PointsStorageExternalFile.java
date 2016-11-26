package org.example.asteroides.logic;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.example.asteroides.logic.storage_operations.PointsStorageBase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by jamarfal on 9/11/16.
 */

public class PointsStorageExternalFile extends PointsStorageBase {
    private static String FILE = Environment.getExternalStorageDirectory() + "/puntuaciones_externa.txt";

    public PointsStorageExternalFile(Context context) {
        super(context);
    }

    @Override
    public void saveScore(int points, String name, long date) {
        if (isExternalMemoryAvailableForWrite()) {
            FileOutputStream f = null;
            try {
                File file = new File(FILE);
                if (!file.exists()) {
                    file.createNewFile();
                }
                f = new FileOutputStream(file, true);
                String texto = points + " " + name + "\n";
                f.write(texto.getBytes());
            } catch (Exception e) {
                Log.e("Asteroides", e.getMessage(), e);
            } finally {
                if (f != null) try {
                    f.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(context, "La memoria no está disponible", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public Vector<String> scoreList(int cantidad) {
        Vector<String> result = new Vector<String>();
        if (isExternalMemoryAvailable()) {
            FileInputStream f = null;
            try {
                File file = new File(FILE);
                if (file.exists()) {
                    f = new FileInputStream(file);
                    BufferedReader input = new BufferedReader(new InputStreamReader(f));
                    int n = 0;
                    String line;
                    do {
                        line = input.readLine();
                        if (line != null) {
                            result.add(line);
                            n++;
                        }
                    } while (n < cantidad && line != null);
                }
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
        } else {
            Toast.makeText(context, "La memoria no está disponible", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    public boolean isExternalMemoryAvailable() {
        String stateSd = Environment.getExternalStorageState();
        return stateSd.equalsIgnoreCase(Environment.MEDIA_MOUNTED);
    }

    public boolean isExternalMemoryAvailableForWrite() {
        String stateSd = Environment.getExternalStorageState();
        return stateSd.equalsIgnoreCase(Environment.MEDIA_MOUNTED) && !stateSd.equalsIgnoreCase(Environment.MEDIA_MOUNTED_READ_ONLY);
    }
}