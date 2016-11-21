package org.example.asteroides.logic;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;

/**
 * Created by jamarfal on 21/11/16.
 */

public class AlmacenPuntuacionesSW_PHP implements PointsStorage {

    private final String SERVER_URL = "http://asteroides.esy.es/asteroides/";

    @Override
    public Vector<String> scoreList(int cantidad) {
        Vector<String> result = new Vector<String>();

        HttpURLConnection conexion = null;
        try {
            URL url = new URL(SERVER_URL + "lista.php?max=20");
            conexion = (HttpURLConnection) url.openConnection();
            if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String linea = reader.readLine();
                while (!linea.equals("")) {
                    result.add(linea);
                    linea = reader.readLine();
                }
                reader.close();
            } else {
                Log.e("Asteroides", conexion.getResponseMessage());
            }
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        } finally {
            if (conexion != null) conexion.disconnect();
            return result;
        }
    }

    @Override
    public void saveScore(int puntos, String nombre, long fecha) {
        HttpURLConnection conexion = null;
        try {
            URL url = new URL(SERVER_URL + "nueva.php?"
                    + "puntos=" + puntos
                    + "&nombre=" + URLEncoder.encode(nombre, "UTF-8") + "&fecha=" + fecha);
            conexion = (HttpURLConnection) url
                    .openConnection();
            if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String linea = reader.readLine();
                if (!linea.equals("OK")) {
                    Log.e("Asteroides", "Error en servicio Web nueva");
                }
            } else {
                Log.e("Asteroides", conexion.getResponseMessage());
            }
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        } finally {
            if (conexion != null)
                conexion.disconnect();
        }
    }
}
