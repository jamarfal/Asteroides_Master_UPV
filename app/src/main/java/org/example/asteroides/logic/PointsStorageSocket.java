package org.example.asteroides.logic;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by jamarfal on 21/11/16.
 */

public class PointsStorageSocket implements PointsStorage {

    private static final String SERVER = "158.42.146.127";
    public static final int PORT = 1234;
    private DowloaderScore dowloaderScore;
    private Context context;

    public PointsStorageSocket(DowloaderScore dowloaderScore, Context context) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        this.dowloaderScore = dowloaderScore;
        this.context = context;
    }


    @Override
    public void saveScore(int points, String name, long date) {
        try {
            Socket sk = new Socket(SERVER, PORT);
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
    public void scoreList(int amount, DowloaderScore dowloaderScore) {
        try {
            GetScoreFromServerTask task = new GetScoreFromServerTask(dowloaderScore);
            task.execute();
        } catch (CancellationException e) {
            Toast.makeText(context, "Error al conectar con servidor", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error con tarea asíncrona",
                    Toast.LENGTH_LONG).show();
        }

    }

    private class GetScoreFromServerTask extends AsyncTask<Integer, Void, Vector<String>> {

        DowloaderScore dowloaderScore;

        public GetScoreFromServerTask(DowloaderScore dowloaderScore) {
            this.dowloaderScore = dowloaderScore;
        }

        @Override
        protected Vector<String> doInBackground(Integer... amount) {
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
                } while (n < amount[0] && response != null);
                sk.close();
            } catch (Exception e) {
                Log.e("Asteroides", e.toString(), e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Vector<String> strings) {
            super.onPostExecute(strings);
            dowloaderScore.OnDowloadScoreComplete(strings);
        }
    }
}
