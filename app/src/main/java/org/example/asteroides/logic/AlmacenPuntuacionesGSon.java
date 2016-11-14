package org.example.asteroides.logic;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by jamarfal on 14/11/16.
 */

public class AlmacenPuntuacionesGSon implements PointsStorage {

    private String string;
    private Clase objeto = new Clase();
    private Gson gson = new Gson();
    private static String FICHERO = Environment.getExternalStorageDirectory() + "/puntuaciones.txt";

    private Type type = new TypeToken<Clase>() {
    }.getType();
    private Context context;

    public AlmacenPuntuacionesGSon(Context context) {
        this.context = context;
        this.objeto = getObjetoFromJson();
    }

    @Override
    public void saveScore(int puntos, String nombre, long fecha) {
        this.objeto.puntuaciones.add(new Puntuacion(puntos, nombre, fecha));
        string = gson.toJson(this.objeto, type);
        guardarString();
    }


    private void guardarString() {
        if (isExternalMemoryAvailable()) {
            FileOutputStream f = null;
            File file = null;
            try {
                file = new File(FICHERO);
                if (!file.exists()) {
                    file.createNewFile();
                }
                f = new FileOutputStream(file);
                f.write(string.getBytes());
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
        string = leerString();
        Vector<String> salida = new Vector<>();
        this.objeto = getObjetoFromJson();
        for (Puntuacion puntuacion : this.objeto.puntuaciones) {
            salida.add(puntuacion.getPuntos() + " " + puntuacion.getNombre());
        }

        return salida;
    }

    private String leerString() {
        String result = "";
        if (isExternalMemoryAvailable()) {
            FileInputStream f = null;
            File file = null;
            try {
                file = new File(FICHERO);
                if (file.exists()) {
                    f = new FileInputStream(file);
                    BufferedReader entrada = new BufferedReader(new InputStreamReader(f));
                    String linea;
                    do {
                        linea = entrada.readLine();
                        if (linea != null) {
                            result += linea;
                        }
                    } while (linea != null);
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

    private Clase getObjetoFromJson() {
        Clase clase = new Clase();
        string = leerString();
        try {
            if (string != null && !string.isEmpty()) {
                clase = gson.fromJson(string, type);
            }
        } catch (Exception exception) {
            Log.e("Asteroides", exception.getMessage(), exception);
        }
        return clase;
    }

    public class Clase {
        private ArrayList<Puntuacion> puntuaciones = new ArrayList<>();
        private boolean guardado;
    }

}
