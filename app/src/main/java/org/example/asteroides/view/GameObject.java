package org.example.asteroides.view;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import java.util.Vector;

/**
 * Created by jamarfal on 24/10/16.
 */

public abstract class GameObject {

    private GraphicGame graphicGame;

    public GameObject(GraphicGame graphicGame) {
        this.graphicGame = graphicGame;
    }

    public GraphicGame getGraphicGame() {
        return graphicGame;
    }

    public void setGraphicGame(GraphicGame graphicGame) {
        this.graphicGame = graphicGame;
    }

    public void setDrawable(Drawable drawable) {
        this.graphicGame.setDrawable(drawable);
    }

    public void positionIn(int x, int y) {
        graphicGame.setCenX(x);
        graphicGame.setCenY(y);
    }

    public double distance(GraphicGame otherGraphicGame) {
        return graphicGame.distance(otherGraphicGame);
    }

    public void draw(Canvas canvas) {
        graphicGame.drawGraphic(canvas);
    }

    public void setVelocity(double x, double y) {
        graphicGame.setIncX(x);
        graphicGame.setIncY(y);
    }

    public void setVelocityInAxisY(double velocity) {
        graphicGame.setIncY(velocity);
    }

    public void setVelocityInAxisX(double velocity) {
        graphicGame.setIncX(velocity);
    }

    public double getIncX() {
        return graphicGame.getIncX();
    }

    public double getIncY() {
        return graphicGame.getIncY();
    }

    public void setAngle(double angle) {
        graphicGame.setAngle(angle);
    }

    public double getAngle() {
        return graphicGame.getAngle();
    }

    public void setRotacion(double rotacion) {
        graphicGame.setRotacion(rotacion);
    }

    public boolean checkCollision(GraphicGame otherGraphic) {
        return this.graphicGame.checkCollision(otherGraphic);
    }

    public abstract void move(double retardation);
}
