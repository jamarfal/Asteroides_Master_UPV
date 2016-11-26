package org.example.asteroides.logic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

/**
 * Created by jamarfal on 14/11/16.
 */

public class PointsStorageSqlite extends SQLiteOpenHelper implements PointsStorage {
    public PointsStorageSqlite(Context context) {
        super(context, "puntuaciones", null, 1);
    }

    //Métodos de SQLiteOpenHelper
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE puntuaciones (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "puntos INTEGER, nombre TEXT, fecha LONG)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        // En caso de una nueva versión habría que actualizar las tablas
    }


    @Override
    public void storeScore(int points, String name, long date, StorageOperations storageOperations) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO puntuaciones VALUES ( null, " +
                points + ", '" + name + "', " + date + ")");
        db.close();
    }

    @Override
    public void getScore(int amount, StorageOperations storageOperations) {
        Vector<String> result = new Vector<String>();
        SQLiteDatabase db = getReadableDatabase();
        String[] FIELDS = {"puntos", "nombre"};
        Cursor cursor = db.query("puntuaciones", FIELDS, null, null,
                null, null, "puntos DESC", Integer.toString(amount));
        while (cursor.moveToNext()) {
            result.add(cursor.getInt(0) + " " + cursor.getString(1));
        }
        cursor.close();
        db.close();
        storageOperations.OnDowloadScoreComplete(result);
    }
}