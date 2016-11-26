package org.example.asteroides.logic;

import android.content.Context;
import android.util.Log;

import org.example.asteroides.logic.storage_operations.PointsStorageBase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;

/**
 * Created by jamarfal on 21/11/16.
 */

public class PoinstStorageSW_PHP extends PointsStorageBase {

    private String serverUrl = "http://158.42.146.127/puntuaciones/";

    public PoinstStorageSW_PHP(Context context, String serverUrl) {
        super(context);
        this.serverUrl = serverUrl;
    }

    @Override
    protected Vector<String> scoreList(int amount) {
        Vector<String> result = new Vector<String>();

        HttpURLConnection connection = null;
        try {
            URL url = new URL(serverUrl + "lista.php?max=20");
            connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = reader.readLine();
                while (!line.equals("")) {
                    result.add(line);
                    line = reader.readLine();
                }
                reader.close();
            } else {
                Log.e("Asteroides", connection.getResponseMessage());
            }
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        } finally {
            if (connection != null) connection.disconnect();
        }
        return result;
    }

    @Override
    public void saveScore(int points, String name, long fecdatea) {
        HttpURLConnection conecction = null;
        try {
            URL url = new URL(serverUrl + "nueva.php?"
                    + "puntos=" + points
                    + "&nombre=" + URLEncoder.encode(name, "UTF-8") + "&fecha=" + fecdatea);
            conecction = (HttpURLConnection) url
                    .openConnection();
            if (conecction.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conecction.getInputStream()));
                String line = reader.readLine();
                if (!line.equals("OK")) {
                    Log.e("Asteroides", "Error en servicio Web nueva");
                }
            } else {
                Log.e("Asteroides", conecction.getResponseMessage());
            }
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        } finally {
            if (conecction != null)
                conecction.disconnect();
        }
    }
}
