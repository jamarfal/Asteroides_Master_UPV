package org.example.asteroides.view;

import android.graphics.Canvas;

import org.example.asteroides.logic.GraphicGame;

/**
 * Created by jamarfal on 20/10/16.
 */

public class Misil {
    private static int STEP_VELOCITY_MISIL = 12;
    private GraphicGame graphicGame;
    private GraphicGame graphicGameOwner;
    private boolean active;
    private int timeMisil;

    public Misil(GraphicGame graphicGame, GraphicGame graphicGameOwner) {
        this.graphicGame = graphicGame;
        this.graphicGameOwner = graphicGameOwner;
    }

    public GraphicGame getGraphicGame() {
        return graphicGame;
    }

    public void setGraphicGame(GraphicGame graphicGame) {
        this.graphicGame = graphicGame;
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

    public void draw(Canvas canvas) {
        graphicGame.drawGraphic(canvas);
    }

    public void updatePositions(double retardation) {
        this.graphicGame.increasePosition(retardation);
        timeMisil -= retardation;
    }

    public boolean checkCollision(GraphicGame otherGraphic) {
        return this.graphicGame.checkCollision(otherGraphic);
    }

    public void fire() {
        graphicGame.setCenX(graphicGameOwner.getCenX());
        graphicGame.setCenY(graphicGameOwner.getCenY());
        graphicGame.setAngle(graphicGameOwner.getAngle());
        graphicGame.setIncX(Math.cos(Math.toRadians(graphicGame.getAngle())) *
                STEP_VELOCITY_MISIL);
        graphicGame.setIncY(Math.sin(Math.toRadians(graphicGame.getAngle())) *
                STEP_VELOCITY_MISIL);
    }
}
