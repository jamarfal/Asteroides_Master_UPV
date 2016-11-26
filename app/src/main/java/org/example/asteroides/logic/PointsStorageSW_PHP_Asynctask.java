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
import java.util.concurrent.TimeUnit;

/**
 * Created by jamarfal on 21/11/16.
 */

public class PointsStorageSW_PHP_Asynctask extends PointsStorageBase {

    private String serverUrl;

    public PointsStorageSW_PHP_Asynctask(Context context, String serverUrl) {
        super(context);
        this.serverUrl = serverUrl;
    }

    @Override
    public Vector<String> scoreList(int amount) {

        HttpURLConnection connection = null;
        Vector<String> result = new Vector<String>();
        try {
            URL url = new URL(serverUrl + "lista.php?max=" + amount);
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

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public void saveScore(int points, String name, long date) {
        HttpURLConnection conexion = null;
        try {
            URL url = new URL(
                    serverUrl + "nueva.php" + "?puntos=" + points + "&nombre="
                            + URLEncoder.encode(name, "UTF-8")
                            + "&fecha=" + date);
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

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
