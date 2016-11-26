package org.example.asteroides.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.example.asteroides.logic.storage_operations.PointsStorageBase;

import java.util.Vector;

/**
 * Created by jamarfal on 14/11/16.
 */

public class PointsStorageProvider extends PointsStorageBase {


    public PointsStorageProvider(Context context) {
        super(context);
    }

    @Override
    public void saveScore(int points, String name, long date) {
        Uri uri = Uri.parse(
                "content://org.example.puntuacionesprovider/puntuaciones");
        ContentValues valores = new ContentValues();
        valores.put("nombre", name);
        valores.put("puntos", points);
        valores.put("fecha", date);
        try {
            context.getContentResolver().insert(uri, valores);
        } catch (Exception e) {
            Toast.makeText(context, "Verificar que está instalado ”+ “org.example.puntuacionesprovider", Toast.LENGTH_LONG).show();
            Log.e("Asteroides", "Error: " + e.toString(), e);
        }
    }

    @Override
    public Vector<String> scoreList(int cantidad) {
        Vector<String> result = new Vector<String>();
        Uri uri = Uri.parse(
                "content://org.example.puntuacionesprovider/puntuaciones");
        Cursor cursor = context.getContentResolver().query(uri,
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
