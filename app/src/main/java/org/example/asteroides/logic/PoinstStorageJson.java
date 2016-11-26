package org.example.asteroides.logic;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by jamarfal on 14/11/16.
 */

public class PoinstStorageJson extends PointsStorageBase {
    private String string;
    private static String FILE = Environment.getExternalStorageDirectory() + "/puntuaciones_json.txt";

    public PoinstStorageJson(Context context) {
        super(context);
    }

    @Override
    protected Vector<String> scoreList(int amount) {
        string = readString();
        List<Puntuacion> scores = createJson();

        Vector<String> output = new Vector<>();
        for (Puntuacion puntuacion : scores) {
            output.add(puntuacion.getPuntos() + " " + puntuacion.getNombre());
        }
        return output;
    }

    @Override
    protected void saveScore(int points, String name, long date) {
        string = readString();
        List<Puntuacion> scores = createJson();
        scores.add(new Puntuacion(points, name, date));
        string = saveJson(scores);
        saveString();
    }


    private String saveJson(List<Puntuacion> scores) {
        String string = "";

        try {
            JSONArray jsonArray = new JSONArray();
            for (Puntuacion puntuacion : scores) {
                JSONObject object = new JSONObject();
                object.put("puntos", puntuacion.getPuntos());
                object.put("nombre", puntuacion.getNombre());
                object.put("fecha", puntuacion.getFecha());
                jsonArray.put(object);
            }
            string = jsonArray.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return string;
    }

    private List<Puntuacion> createJson() {
        List<Puntuacion> scores = new ArrayList<>();
        try {
            JSONArray json_array = new JSONArray(string);
            for (int i = 0; i < json_array.length(); i++) {
                JSONObject objeto = json_array.getJSONObject(i);
                scores.add(new Puntuacion(objeto.getInt("puntos"),
                        objeto.getString("nombre"), objeto.getLong("fecha")));
            }

        } catch (JSONException e) {
            Log.e("Asteroides", e.getMessage(), e);
        }
        return scores;
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

    public boolean isExternalMemoryAvailable() {
        String stateSd = Environment.getExternalStorageState();
        return stateSd.equalsIgnoreCase(Environment.MEDIA_MOUNTED);
    }

    public boolean isExternalMemoryAvailableForWrite() {
        String stateSd = Environment.getExternalStorageState();
        return stateSd.equalsIgnoreCase(Environment.MEDIA_MOUNTED) && !stateSd.equalsIgnoreCase(Environment.MEDIA_MOUNTED_READ_ONLY);
    }
}