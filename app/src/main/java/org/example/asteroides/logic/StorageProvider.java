package org.example.asteroides.logic;

import android.content.Context;

/**
 * Created by jamarfal on 26/11/16.
 */

public class StorageProvider {
    public PointsStorage createStorage(int saveMethodType, Context context) {
        PointsStorage pointsStorage = null;
        switch (saveMethodType) {
            case 0:
                pointsStorage = new PointsStorageArray(context);
                break;
            case 1:
                pointsStorage = new PointsStoragePreferences(context);
                break;
            case 2:
                pointsStorage = new PointsStorageInternalFile(context);
                break;
            case 3:
                pointsStorage = new PointsStorageExternalFile(context);
                break;
            case 4:
                pointsStorage = new PoinstStorageExternalFileApi8(context);
                break;
            case 5:
                pointsStorage = new PointsStorageRawResources(context);
                break;
            case 6:
                pointsStorage = new PointsStorageAssetsResources(context);
                break;
            case 7:
                pointsStorage = new PointsStorageXML_SAX(context);
                break;
            case 8:
                pointsStorage = new PointsStorageGson(context);
                break;
            case 9:
                pointsStorage = new PoinstStorageJson(context);
                break;
            case 10:
                pointsStorage = new PointsStorageSqliteRel(context);
                break;
            case 11:
                pointsStorage = new PointsStorageProvider(context);
                break;
            case 12:
                pointsStorage = new PointsStorageSocket(context);
                break;
            case 13:
                pointsStorage = new PoinstStorageSW_PHP(context, "http://158.42.146.127/puntuaciones/");
                break;
            case 14:
                pointsStorage = new PoinstStorageSW_PHP(context, "http://asteroides.esy.es/asteroides/");
                break;
            case 15:
                pointsStorage = new PointsStorageSW_PHP_Asynctask(context, "http://asteroides.esy.es/asteroides/");
                break;
        }
        return pointsStorage;
    }
}
