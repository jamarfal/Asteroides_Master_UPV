package org.example.asteroides.logic;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

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

public class AlmacenPuntuacionesFicheroExternoExtApl implements PointsStorage {
    private static String FICHERO = Environment.getExternalStorageDirectory() + "/Android/data/org.example.asteroides/files/puntuaciones.txt";
    private Context context;

    public AlmacenPuntuacionesFicheroExternoExtApl(Context context) {
        this.context = context;
    }

    @Override
    public void saveScore(int puntos, String nombre, long fecha) {
        if (isExternalMemoryAvailable()) {
            FileOutputStream f = null;


            try {
                File ruta = new File(FICHERO);
                if (!ruta.exists()) {
                    ruta.mkdirs();
                }

                f = new FileOutputStream(ruta, true);
                String texto = puntos + " " + nombre + "\n";
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
                File ruta = new File(FICHERO);
                if (!ruta.exists()) {
                    ruta.mkdirs();
                }
                f = new FileInputStream(ruta);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(f));
                int n = 0;
                String linea;
                do {
                    linea = entrada.readLine();
                    if (linea != null) {
                        result.add(linea);
                        n++;
                    }
                } while (n < cantidad && linea != null);

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
}