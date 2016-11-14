package org.example.asteroides.logic;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by jamarfal on 14/11/16.
 */

public class AlmacenPuntuacionesGSon implements PointsStorage {

    private String string;
    private ArrayList<Puntuacion> puntuaciones = new ArrayList<>();
    private Gson gson = new Gson();

    private Type type = new TypeToken<List<Puntuacion>>() {
    }.getType();
    private Context context;

    public AlmacenPuntuacionesGSon(Context context) {
        this.context = context;
        saveScore(45000, "Mi nombre", System.currentTimeMillis());
        saveScore(31000, "Otro nombre", System.currentTimeMillis());

    }

    @Override
    public void saveScore(int puntos, String nombre, long fecha) {
        puntuaciones.add(new Puntuacion(puntos, nombre, fecha));
        string = gson.toJson(puntuaciones, type);
        //guardarString();
    }

    @Override
    public Vector<String> scoreList(int cantidad) {
        //string = leerString();
        puntuaciones = gson.fromJson(string, type);

        Vector<String> salida = new Vector<>();

        for (Puntuacion puntuacion : puntuaciones) {
            salida.add(puntuacion.getPuntos() + " " + puntuacion.getNombre());
        }


        return salida;

    }

}
