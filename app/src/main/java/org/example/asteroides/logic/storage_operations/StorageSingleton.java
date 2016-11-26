package org.example.asteroides.logic.storage_operations;

import org.example.asteroides.logic.provider.StorageProvider;

/**
 * Created by jamarfal on 26/11/16.
 */
public class StorageSingleton {


    private PointsStorage pointsStorage;

    private static StorageSingleton ourInstance;


    public static StorageSingleton getInstance() {

        if (ourInstance == null) {
            ourInstance = new StorageSingleton();
        }
        return ourInstance;
    }

    private StorageSingleton() {
    }

    public PointsStorage getPointsStorage() {
        return pointsStorage;
    }

    public void setPointsStorage(PointsStorage pointsStorage) {
        this.pointsStorage = pointsStorage;
    }

}
