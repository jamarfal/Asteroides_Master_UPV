package org.example.asteroides.logic.storage_operations;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Vector;
import java.util.concurrent.CancellationException;

/**
 * Created by jamarfal on 25/11/16.
 */
public abstract class PointsStorageBase implements PointsStorage {
    protected Context context;

    public PointsStorageBase(Context context) {
        this.context = context;
    }

    protected abstract Vector<String> scoreList(int amount);

    protected abstract void saveScore(int points, String name, long date);

    @Override
    public void storeScore(int points, String name, long date, StorageOperations storageOperations) {
        try {
            Log.i("Asteroides", "storeScore");
            SaveScoreTask task = new SaveScoreTask(this, storageOperations, context);
            task.execute(String.valueOf(points), name, String.valueOf(date));
        } catch (CancellationException e) {
            Toast.makeText(context, "Error al conectar con servidor", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("StoreScore", e.getMessage(), e);
            Toast.makeText(context, "Error con tarea asíncrona", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getScore(int amount, StorageOperations storageOperations) {
        try {
            GetScoreTask task = new GetScoreTask(storageOperations, this, context);
            task.execute(amount);
        } catch (CancellationException e) {
            Toast.makeText(context, "Error al conectar con servidor", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("StoreScore", e.getMessage(), e);

            Toast.makeText(context, "Error con tarea asíncrona",
                    Toast.LENGTH_LONG).show();
        }
    }
}
