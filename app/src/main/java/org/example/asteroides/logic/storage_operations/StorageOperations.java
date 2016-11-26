package org.example.asteroides.logic.storage_operations;

import java.util.Vector;

/**
 * Created by jamarfal on 25/11/16.
 */

public interface StorageOperations {
    public void OnDowloadScoreComplete(Vector<String> scoreList);

    public void OnSaveScoreComplete();
}
