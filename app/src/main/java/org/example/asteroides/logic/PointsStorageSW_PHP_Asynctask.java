package org.example.asteroides.logic;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by jamarfal on 21/11/16.
 */

public class PointsStorageSW_PHP_Asynctask implements PointsStorage {

    private String serverUrl;
    private Context context;

    public PointsStorageSW_PHP_Asynctask(Context context, String serverUrl) {
        this.context = context;
        this.serverUrl = serverUrl;
    }

    @Override
    public Vector<String> scoreList(int amount) {


        try {
            GetScoreFromServerTask task = new GetScoreFromServerTask();
            task.execute(amount);
            return task.get(4, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Toast.makeText(context, "Tiempo excedido al conectar",
                    Toast.LENGTH_LONG).show();
        } catch (CancellationException e) {
            Toast.makeText(context, "Error al conectar con servidor", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error con tarea asíncrona",
                    Toast.LENGTH_LONG).show();
        }
        return new Vector<String>();
    }


    @Override
    public void saveScore(int points, String name, long date) {
        try {
            SaveScoreInServerTask task = new SaveScoreInServerTask();
            task.execute(String.valueOf(points), name,
                    String.valueOf(date));
            task.get(4, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Toast.makeText(context, "Tiempo excedido al conectar",
                    Toast.LENGTH_LONG).show();
        } catch (CancellationException e) {
            Toast.makeText(context, "Error al conectar con servidor", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error con tarea asíncrona", Toast.LENGTH_LONG).show();
        }
    }

    private class GetScoreFromServerTask extends AsyncTask<Integer, Void, Vector<String>> {
        @Override
        protected Vector<String> doInBackground(Integer... amount) {
            HttpURLConnection connection = null;
            Vector<String> result = new Vector<String>();
            try {
                URL url = new URL(serverUrl + "lista.php?max=" + amount[0]);
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
                    cancel(true);
                }
            } catch (Exception e) {
                Log.e("Asteroides", e.getMessage(), e);
                cancel(true);
            } finally {
                if (connection != null) connection.disconnect();
            }
            return result;
        }
    }

    private class SaveScoreInServerTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... param) {
            HttpURLConnection conexion = null;
            try {
                URL url = new URL(
                        serverUrl + "nueva.php" + "?puntos=" + param[0] + "&nombre="
                                + URLEncoder.encode(param[1], "UTF-8")
                                + "&fecha=" + param[2]);
                conexion = (HttpURLConnection) url
                        .openConnection();
                if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                    String linea = reader.readLine();
                    if (!linea.equals("OK")) {
                        Log.e("Asteroides", "Error en servicio Web nueva");
                        cancel(true);
                    }
                } else {
                    Log.e("Asteroides", conexion.getResponseMessage());
                    cancel(true);
                }
            } catch (Exception e) {
                Log.e("Asteroides", e.getMessage(), e);
                cancel(true);
            } finally {
                if (conexion != null)
                    conexion.disconnect();
            }
            return null;
        }
    }
}
