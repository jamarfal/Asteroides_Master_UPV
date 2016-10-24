package org.example.asteroides.view;

import android.content.Context;
import android.graphics.Canvas;

import org.example.asteroides.pool.FxSoundPool;

/**
 * Created by jamarfal on 20/10/16.
 */

public class Misil extends GameObject {
    private static int STEP_VELOCITY_MISIL = 12;
    private GraphicGame graphicGameOwner;
    private boolean active;
    private int timeMisil;

    public Misil(GraphicGame graphicGame) {
        super(graphicGame);
    }

    public Misil(GraphicGame graphicGame, GraphicGame graphicGameOwners) {
        super(graphicGame);
        this.graphicGameOwner = graphicGameOwners;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean activeMisil) {
        this.active = activeMisil;
    }

    public int getTimeMisil() {
        return timeMisil;
    }

    public void setTimeMisil(int timeMisil) {
        this.timeMisil = timeMisil;
    }
    //endregion

    public void fire(int viewWidth, int viewHeight) {

        positionIn(graphicGameOwner.getCenX(), graphicGameOwner.getCenY());
        setAngle((int) graphicGameOwner.getAngle());
        setVelocity(
                Math.cos(Math.toRadians(getAngle())) * STEP_VELOCITY_MISIL,
                Math.sin(Math.toRadians(getAngle())) * STEP_VELOCITY_MISIL
        );

        int timeMisil = (int) Math.min(
                viewWidth / Math.abs(getIncX()),
                viewHeight / Math.abs(getIncY())
        ) - 2;
        setTimeMisil(timeMisil);
        setActive(true);

    }

    @Override
    public void move(double retardation) {
        getGraphicGame().increasePosition(retardation);
        timeMisil -= retardation;
    }


}
