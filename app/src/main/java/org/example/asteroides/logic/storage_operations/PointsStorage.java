package org.example.asteroides.logic.storage_operations;

/**
 * Created by jamarfal on 4/10/16.
 */

public interface PointsStorage {

    public void storeScore(int points, String name, long date, StorageOperations storageOperations);

    public void getScore(int amount, StorageOperations storageOperations);


}
