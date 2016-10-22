package org.example.asteroides.pool;

import org.example.asteroides.logic.GraphicGame;
import org.example.asteroides.view.Misil;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by jamarfal on 20/10/16.
 */

public class PoolObject {

    private List<Misil> objectsInUse;
    private Stack<Integer> objectsFree;

    public PoolObject() {
        objectsInUse = new LinkedList<>();
        objectsFree = new Stack<>();
    }

    public Misil get(GraphicGame misilGraphic, GraphicGame ownerGraphic) {
        Misil object;
        if (objectsFree.size() <= 0) {
            object = new Misil(misilGraphic, ownerGraphic);
            objectsInUse.add(object);
        } else {
            object = objectsInUse.get(objectsFree.pop());
        }
        return object;
    }

    public void free(Misil object) {
        int index = searchObject(object);
        if (index < 0) {
            return;
        }
        objectsFree.add(index);
    }

    private int searchObject(Misil object) {
        for (int i = 0; i < objectsInUse.size(); i++) {
            if (objectsInUse.get(i) == object) {
                return i;
            }
        }
        return -1;
    }

    public List<Misil> getObjects() {
        return objectsInUse;
    }
}
