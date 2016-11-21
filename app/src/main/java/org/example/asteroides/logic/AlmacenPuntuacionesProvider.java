package org.example.asteroides.logic;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.Vector;

/**
 * Created by jamarfal on 14/11/16.
 */

public class AlmacenPuntuacionesProvider implements PointsStorage {

    private Activity activity;

    public AlmacenPuntuacionesProvider(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void saveScore(int puntos, String nombre, long fecha) {
        Uri uri = Uri.parse(
                "content://org.example.puntuacionesprovider/puntuaciones");
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("puntos", puntos);
        valores.put("fecha", fecha);
        try {
            activity.getContentResolver().insert(uri, valores);
        } catch (Exception e) {
            Toast.makeText(activity, "Verificar que está instalado ”+ “org.example.puntuacionesprovider", Toast.LENGTH_LONG).show();
            Log.e("Asteroides", "Error: " + e.toString(), e);
        }
    }

    @Override
    public Vector<String> scoreList(int cantidad) {
        Vector<String> result = new Vector<String>();
        Uri uri = Uri.parse(
                "content://org.example.puntuacionesprovider/puntuaciones");
        Cursor cursor = activity.getContentResolver().query(uri,
                null, null, null, "fecha DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String nombre = cursor.getString(
                        cursor.getColumnIndex("nombre"));
                int puntos = cursor.getInt(cursor.getColumnIndex("puntos"));
                result.add(puntos + " " + nombre);
            }
        }
        return result;
    }

}
