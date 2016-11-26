package org.example.asteroides.logic;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by jamarfal on 25/11/16.
 */

public class SaveScoreTask extends AsyncTask<String, Void, Void> {

    PointsStorageBase pointsStorageBase;
    StorageOperations storageOperations;

    public SaveScoreTask(PointsStorageBase pointsStorageBase, StorageOperations saverScore) {
        this.pointsStorageBase = pointsStorageBase;
        this.storageOperations = saverScore;
    }

    @Override
    protected Void doInBackground(String... param) {
        pointsStorageBase.saveScore(Integer.valueOf(param[0]), param[1], Long.valueOf(param[2]));
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        storageOperations.OnSaveScoreComplete();
    }
}
