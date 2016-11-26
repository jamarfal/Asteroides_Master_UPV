package org.example.asteroides.logic.storage_operations;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by jamarfal on 25/11/16.
 */

public class SaveScoreTask extends AsyncTask<String, Void, Void> {

    PointsStorageBase pointsStorageBase;
    StorageOperations storageOperations;
    ProgressDialog progressDialog;
    Context context;

    public SaveScoreTask(PointsStorageBase pointsStorageBase, StorageOperations saverScore, Context context) {
        this.context = context;
        this.pointsStorageBase = pointsStorageBase;
        this.storageOperations = saverScore;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Obteniendo puntuación");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... param) {
        Log.i("Asteroides", "doInBackground");
        pointsStorageBase.saveScore(Integer.valueOf(param[0]), param[1], Long.valueOf(param[2]));
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        storageOperations.OnSaveScoreComplete();
    }
}
