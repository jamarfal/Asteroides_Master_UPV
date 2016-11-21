package org.example.asteroides.logic;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by jamarfal on 14/11/16.
 */

public class AlmacenPuntuacionesJson implements PointsStorage {
    private String string;
    private Context context;
    private ArrayList<Puntuacion> puntuaciones = new ArrayList<>();

    public AlmacenPuntuacionesJson(Context context) {
        this.context = context;
        saveScore(45000, "Mi nombre", System.currentTimeMillis());
        saveScore(31000, "Otro nombre", System.currentTimeMillis());
    }

    @Override
    public void saveScore(int puntos, String nombre, long fecha) {
        puntuaciones.add(new Puntuacion(puntos, nombre, fecha));
        string = guardarJSon();
        //guardarString();
    }

    @Override
    public Vector<String> scoreList(int cantidad) {

        //string = leerFichero();

        leerJSon(string);

        Vector<String> salida = new Vector<>();
        for (Puntuacion puntuacion : puntuaciones) {
            salida.add(puntuacion.getPuntos() + " " + puntuacion.getNombre());
        }
        return salida;
    }

    private String guardarJSon() {
        String string = "";

        try {
            JSONArray jsonArray = new JSONArray();
            for (Puntuacion puntuacion : puntuaciones) {
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

    private void leerJSon(String string) {
        try {
            JSONArray json_array = new JSONArray(string);
            puntuaciones = new ArrayList<>();
            for (int i = 0; i < json_array.length(); i++) {
                JSONObject objeto = json_array.getJSONObject(i);
                puntuaciones.add(new Puntuacion(objeto.getInt("puntos"),
                        objeto.getString("nombre"), objeto.getLong("fecha")));
            }

        } catch (JSONException e) {
        }
    }
}