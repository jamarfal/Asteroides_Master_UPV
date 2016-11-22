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

public class PointsStorageGson implements PointsStorage {

    private String string;
    private ParserClass object = new ParserClass();
    private Gson gson = new Gson();
    private static String FILE = Environment.getExternalStorageDirectory() + "/puntuaciones.txt";

    private Type type = new TypeToken<ParserClass>() {
    }.getType();
    private Context context;

    public PointsStorageGson(Context context) {
        this.context = context;
        this.object = getObjetoFromJson();
    }

    @Override
    public void saveScore(int points, String name, long date) {
        this.object.puntuaciones.add(new Puntuacion(points, name, date));
        string = gson.toJson(this.object, type);
        saveString();
    }


    private void saveString() {
        if (isExternalMemoryAvailableForWrite()) {
            FileOutputStream f = null;
            try {
                File file = new File(FILE);
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
        string = readString();
        Vector<String> salida = new Vector<>();
        this.object = getObjetoFromJson();
        for (Puntuacion puntuacion : this.object.puntuaciones) {
            salida.add(puntuacion.getPuntos() + " " + puntuacion.getNombre());
        }

        return salida;
    }

    private String readString() {
        String result = "";
        if (isExternalMemoryAvailable()) {
            FileInputStream f = null;
            try {
                File file = new File(FILE);
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

    private ParserClass getObjetoFromJson() {
        ParserClass parserClass = new ParserClass();
        string = readString();
        try {
            if (string != null && !string.isEmpty()) {
                parserClass = gson.fromJson(string, type);
            }
        } catch (Exception exception) {
            Log.e("Asteroides", exception.getMessage(), exception);
        }
        return parserClass;
    }

    public class ParserClass {
        private ArrayList<Puntuacion> puntuaciones = new ArrayList<>();
        private boolean guardado;
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
