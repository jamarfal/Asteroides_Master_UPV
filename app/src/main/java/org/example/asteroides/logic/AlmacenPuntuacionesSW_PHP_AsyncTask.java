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

public class AlmacenPuntuacionesSW_PHP_AsyncTask implements PointsStorage {

    private final String SERVER_URL = "http://asteroides.esy.es/asteroides/";
    private Context context;

    public AlmacenPuntuacionesSW_PHP_AsyncTask(Context context) {
        this.context = context;
    }

    @Override
    public Vector<String> scoreList(int cantidad) {


        try {
            TareaLista tarea = new TareaLista();
            tarea.execute(cantidad);
            return tarea.get(4, TimeUnit.SECONDS);
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
    public void saveScore(int puntos, String nombre, long fecha) {
        try {
            TareaGuardar tarea = new TareaGuardar();
            tarea.execute(String.valueOf(puntos), nombre,
                    String.valueOf(fecha));
            tarea.get(4, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Toast.makeText(context, "Tiempo excedido al conectar",
                    Toast.LENGTH_LONG).show();
        } catch (CancellationException e) {
            Toast.makeText(context, "Error al conectar con servidor", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error con tarea asíncrona", Toast.LENGTH_LONG).show();
        }
    }

    private class TareaLista extends AsyncTask<Integer, Void, Vector<String>> {
        @Override
        protected Vector<String> doInBackground(Integer... cantidad) {
            HttpURLConnection conexion = null;
            Vector<String> result = new Vector<String>();
            try {
                URL url = new URL(SERVER_URL + "lista.php?max=" + cantidad[0]);
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
                    cancel(true);
                }
            } catch (Exception e) {
                Log.e("Asteroides", e.getMessage(), e);
                cancel(true);
            } finally {
                if (conexion != null) conexion.disconnect();
                return result;
            }
        }
    }

    private class TareaGuardar extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... param) {
            HttpURLConnection conexion = null;
            try {
                URL url = new URL(
                        SERVER_URL + "nueva.php" + "?puntos=" + param[0] + "&nombre="
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
