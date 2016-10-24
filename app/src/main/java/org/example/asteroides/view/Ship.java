package org.example.asteroides.view;

import android.graphics.Canvas;

/**
 * Created by jamarfal on 24/10/16.
 */

public class Ship {

    private GraphicGame graphicGame;
    private int turnShip; // Incremento de dirección
    private double shipAcceleration; // aumento de velocidad
    private static final int MAX_SHIP_VELOCITY = 20;
    // Incremento estándar de giro y aceleración
    private static final int STEP_TURN_SHIP = 5;
    private static final float STEP_ACCELERATION_SHIP = 0.5f;

    public Ship(GraphicGame ship) {
        this.graphicGame = ship;
    }

    public int getTurnShip() {
        return turnShip;
    }

    public void setTurnShip(int turnShip) {
        this.turnShip = turnShip;
    }

    public double getShipAcceleration() {
        return shipAcceleration;
    }

    public void setShipAcceleration(double shipAcceleration) {
        this.shipAcceleration = shipAcceleration;
    }

    public GraphicGame getGraphicGame() {
        return graphicGame;
    }

    public void move(double retardation) {
        // Actualizamos velocidad y dirección de la nave a partir de
        // giroNave y aceleracionNave (según la entrada del jugador)
        updateVelocityAndDirection(retardation);
        graphicGame.increasePosition(retardation); // Actualizamos posición
    }

    private void updateVelocityAndDirection(double retardation) {

        graphicGame.setAngle((int) (graphicGame.getAngle() + turnShip * retardation));

        double nIncX = graphicGame.getIncX() + shipAcceleration *
                Math.cos(Math.toRadians(graphicGame.getAngle())) * retardation;
        double nIncY = graphicGame.getIncY() + shipAcceleration *
                Math.sin(Math.toRadians(graphicGame.getAngle())) * retardation;

        // Actualizamos si el módulo de la velocidad no excede el máximo
        if (Math.hypot(nIncX, nIncY) <= MAX_SHIP_VELOCITY) {
            graphicGame.setIncX(nIncX);
            graphicGame.setIncY(nIncY);
        }
    }


    public void positionIn(int x, int y) {
        graphicGame.setCenX(x);
        graphicGame.setCenY(y);
    }

    public void draw(Canvas canvas) {
        graphicGame.drawGraphic(canvas);
    }
}
