package org.example.asteroides.logic;

import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by jamarfal on 21/11/16.
 */

public class AlmacenPuntuacionesSocket implements PointsStorage {

    private static final String SERVIDOR = "158.42.146.127";

    public AlmacenPuntuacionesSocket() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }


    @Override
    public void saveScore(int puntos, String nombre, long fecha) {
        try {
            Socket sk = new Socket(SERVIDOR, 1234);
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(sk.getInputStream()));
            PrintWriter salida = new PrintWriter(
                    new OutputStreamWriter(sk.getOutputStream()), true);
            salida.println(puntos + " " + nombre);
            String respuesta = entrada.readLine();
            if (!respuesta.equals("OK")) {
                Log.e("Asteroides", "Error: respuesta de servidor incorrecta");
            }
            sk.close();
        } catch (Exception e) {
            Log.e("Asteroides", e.toString(), e);
        }
    }

    @Override
    public Vector<String> scoreList(int cantidad) {
        Vector<String> result = new Vector<String>();
        try {
            Socket sk = new Socket(SERVIDOR, 1234);
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(sk.getInputStream()));
            PrintWriter salida = new PrintWriter(
                    new OutputStreamWriter(sk.getOutputStream()), true);
            salida.println("PUNTUACIONES");
            int n = 0;
            String respuesta;
            do {
                respuesta = entrada.readLine();
                if (respuesta != null) {
                    result.add(respuesta);
                    n++;
                }
            } while (n < cantidad && respuesta != null);
            sk.close();
        } catch (Exception e) {
            Log.e("Asteroides", e.toString(), e);
        }
        return result;
    }
}
