package org.example.asteroides.view;

import android.graphics.Canvas;

/**
 * Created by jamarfal on 24/10/16.
 */

public class Ship extends GameObject {

    private int turnShip; // Incremento de dirección
    private double shipAcceleration; // aumento de velocidad
    private static final int MAX_SHIP_VELOCITY = 20;
    // Incremento estándar de giro y aceleración
    public static final int STEP_TURN_SHIP = 5;
    public static final float STEP_ACCELERATION_SHIP = 0.5f;

    public Ship(GraphicGame ship) {
        super(ship);
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


    private void updateVelocityAndDirection(double retardation) {

        setAngle((int) (getAngle() + turnShip * retardation));

        double nIncX = getIncX() + shipAcceleration *
                Math.cos(Math.toRadians(getAngle())) * retardation;
        double nIncY = getIncY() + shipAcceleration *
                Math.sin(Math.toRadians(getAngle())) * retardation;

        // Actualizamos si el módulo de la velocidad no excede el máximo
        if (Math.hypot(nIncX, nIncY) <= MAX_SHIP_VELOCITY) {
            setVelocity(nIncX, nIncY);
        }
    }

    @Override
    public void move(double retardation) {
        // Actualizamos velocidad y dirección de la nave a partir de
        // giroNave y aceleracionNave (según la entrada del jugador)
        updateVelocityAndDirection(retardation);
        getGraphicGame().increasePosition(retardation); // Actualizamos posición
    }

}
