package org.example.asteroides.view;

import android.graphics.Canvas;

/**
 * Created by jamarfal on 24/10/16.
 */
public class Asteroid extends GameObject {

    private int numFragments;

    public Asteroid(GraphicGame graphicGame, int numFragments) {
        super(graphicGame);
        this.numFragments = numFragments;
    }


    public int getNumFragments() {
        return numFragments;
    }

    public void setNumFragments(int numFragments) {
        this.numFragments = numFragments;
    }

    @Override
    public void move(double retardation) {
        getGraphicGame().increasePosition(retardation);
    }
}
