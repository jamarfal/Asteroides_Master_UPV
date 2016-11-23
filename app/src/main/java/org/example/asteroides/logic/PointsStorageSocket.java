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

public class PointsStorageSocket implements PointsStorage {

    private static final String SERVER = "158.42.146.127";
    public static final int PORT = 1234;

    public PointsStorageSocket() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }


    @Override
    public void saveScore(int points, String name, long date) {
        try {
            Socket sk = new Socket(SERVER, 1234);
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(sk.getInputStream()));
            PrintWriter output = new PrintWriter(
                    new OutputStreamWriter(sk.getOutputStream()), true);
            output.println(points + " " + name);
            String response = input.readLine();
            if (!response.equals("OK")) {
                Log.e("Asteroides", "Error: respuesta de servidor incorrecta");
            }
            sk.close();
        } catch (Exception e) {
            Log.e("Asteroides", e.toString(), e);
        }
    }

    @Override
    public Vector<String> scoreList(int amount) {
        Vector<String> result = new Vector<String>();
        try {
            Socket sk = new Socket(SERVER, PORT);
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(sk.getInputStream()));
            PrintWriter output = new PrintWriter(
                    new OutputStreamWriter(sk.getOutputStream()), true);
            output.println("PUNTUACIONES");
            int n = 0;
            String response;
            do {
                response = input.readLine();
                if (response != null) {
                    result.add(response);
                    n++;
                }
            } while (n < amount && response != null);
            sk.close();
        } catch (Exception e) {
            Log.e("Asteroides", e.toString(), e);
        }
        return result;
    }
}
