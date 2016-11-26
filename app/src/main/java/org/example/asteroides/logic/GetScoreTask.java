package org.example.asteroides.logic;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.Vector;

/**
 * Created by jamarfal on 25/11/16.
 */

public class GetScoreTask extends AsyncTask<Integer, Void, Vector<String>> {


    StorageOperations storageOperations;
    PointsStorageBase pointsStorageBase;
    ProgressDialog progressDialog;
    Context context;


    public GetScoreTask(StorageOperations storageOperations, PointsStorageBase pointsStorageBase, Context context) {
        this.storageOperations = storageOperations;
        this.pointsStorageBase = pointsStorageBase;
        this.context = context;
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
    protected Vector<String> doInBackground(Integer... amount) {
        return pointsStorageBase.scoreList(amount[0]);
    }

    @Override
    protected void onPostExecute(Vector<String> strings) {
        super.onPostExecute(strings);
        progressDialog.dismiss();
        storageOperations.OnDowloadScoreComplete(strings);
    }
}
