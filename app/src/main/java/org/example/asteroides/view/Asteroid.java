package org.example.asteroides.view;

import android.graphics.Canvas;

/**
 * Created by jamarfal on 24/10/16.
 */

public class Asteroid {

    private GraphicGame graphicGame;
    private int numFragments;// Fragmentos en que se divide

    public Asteroid(GraphicGame graphicGame, int numFragments) {
        this.graphicGame = graphicGame;
        this.numFragments = numFragments;
    }

    public void positionIn(int x, int y) {
        graphicGame.setCenY(x);
        graphicGame.setCenY(y);
    }

    public GraphicGame getGraphicGame() {
        return graphicGame;
    }

    public void setGraphicGame(GraphicGame graphicGame) {
        this.graphicGame = graphicGame;
    }

    public void setAngle(int angle) {
        graphicGame.setAngle(angle);
    }

    public void setRotacion(int rotation) {
        graphicGame.setRotacion(rotation);
    }

    public void move(double retardation) {
        graphicGame.increasePosition(retardation);
    }

    public double distance(GraphicGame otherGraphicGame) {
        return graphicGame.distance(otherGraphicGame);
    }

    public void draw(Canvas canvas) {
        graphicGame.drawGraphic(canvas);
    }
}
