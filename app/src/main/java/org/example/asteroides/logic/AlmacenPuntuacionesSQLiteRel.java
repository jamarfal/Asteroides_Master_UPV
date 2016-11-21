package org.example.asteroides.logic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;

import java.util.Vector;

/**
 * Created by jamarfal on 14/11/16.
 */

public class AlmacenPuntuacionesSQLiteRel extends SQLiteOpenHelper implements PointsStorage {

    public AlmacenPuntuacionesSQLiteRel(Context context) {
        super(context, "puntuaciones", null, 2);
    }

    //Métodos de SQLiteOpenHelper
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios (" +
                "usu_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, correo TEXT)");
        db.execSQL("CREATE TABLE puntuaciones2 (" +
                "pun_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "puntos INTEGER, fecha LONG, usuario INTEGER, " +
                "FOREIGN KEY (usuario) REFERENCES usuarios (usu_id))");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            onCreate(db); //Crea las nuevas tablas
            Cursor cursor = db.rawQuery("SELECT puntos, nombre, fecha " +
                    "FROM puntuaciones", null); //Recorre la tabla antigua
            while (cursor.moveToNext()) {
                saveScore(db, cursor.getInt(0), cursor.getString(1),
                        cursor.getInt(2));
            } //Crea los nuevos registros
            cursor.close();
            db.execSQL("DROP TABLE puntuaciones"); //Elimina tabla antigua
        }

    }

    //Métodos de AlmacenPuntuaciones
    @Override
    public Vector<String> scoreList(int cantidad) {
        Vector<String> result = new Vector<String>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT puntos, nombre FROM "
                + "puntuaciones2, usuarios WHERE usuario = usu_id ORDER BY "
                + "puntos DESC LIMIT " + cantidad, null);
        while (cursor.moveToNext()) {
            result.add(cursor.getInt(0) + " " + cursor.getString(1));
        }
        cursor.close();
        db.close();
        return result;
    }

    @Override
    public void saveScore(int puntos, String nombre, long fecha) {
        SQLiteDatabase db = getWritableDatabase();
        saveScore(db, puntos, nombre, fecha);
        db.close();
    }

    public void saveScore(SQLiteDatabase db, int puntos, String nombre, long fecha) {
        int usuario = buscaInserta(db, nombre);
        db.execSQL("PRAGMA foreign_keys = ON");
        db.execSQL("INSERT INTO puntuaciones2 VALUES ( null, " + puntos + ", " + fecha + ", " + usuario + ")");
    }


    private int buscaInserta(SQLiteDatabase db, String nombre) {
        Cursor cursor = db.rawQuery("SELECT usu_id FROM usuarios "
                + "WHERE nombre='" + nombre + "'", null);
        if (cursor.moveToNext()) {
            int result = cursor.getInt(0);
            cursor.close();
            return result;
        } else {
            cursor.close();
            db.execSQL("INSERT INTO usuarios VALUES (null, '" + nombre
                    + "', 'correo@dominio.es')");
            return buscaInserta(db, nombre);
        }
    }
}