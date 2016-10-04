package org.example.asteroides;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import org.example.asteroides.logic.GraphicGame;

import java.util.Vector;

/**
 * Created by jamarfal on 4/10/16.
 */

public class GameView extends View

{
    // //// ASTEROIDES //////
    private Vector<GraphicGame> asteroids; // Vector con los Asteroides
    private int numAsteroids = 5; // Número inicial de asteroids
    private int numFragments = 3; // Fragmentos en que se divide
    private GraphicGame ship;
    private int turnShip; // Incremento de dirección
    private double shipAcceleration; // aumento de velocidad
    private static final int MAX_SHIP_VELOCITY = 20;
    // Incremento estándar de giro y aceleración
    private static final int STEP_TURN_SHIP = 5;
    private static final float STEP_ACCELERATION_SHIP = 0.5f;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable drawableShip, drawableAsteroid, drawableMisil;
        drawableAsteroid = context.getResources().getDrawable(
                R.drawable.asteroide1);
        drawableShip = context.getResources().getDrawable(R.drawable.nave);
        ship = new GraphicGame(this, drawableShip);
        asteroids = new Vector<GraphicGame>();

        for (int i = 0; i < numAsteroids; i++) {
            GraphicGame asteroid = new GraphicGame(this, drawableAsteroid);
            asteroid.setIncY(Math.random() * 4 - 2);
            asteroid.setIncX(Math.random() * 4 - 2);
            asteroid.setAngle((int) (Math.random() * 360));
            asteroid.setRotacion((int) (Math.random() * 8 - 4));
            asteroids.add(asteroid);
        }
    }

    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anter,
                                 int alto_anter) {
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);
        // Una vez que conocemos nuestro ancho y alto.
        for (GraphicGame asteroide : asteroids) {
            do {
                asteroide.setCenX((int) (Math.random() * ancho));
                asteroide.setCenY((int) (Math.random() * alto));
            } while (asteroide.distance(ship) < (ancho + alto) / 5);
        }

        ship.setCenX(ancho / 2);
        ship.setCenY(alto / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (GraphicGame asteroide : asteroids) {
            asteroide.drawGraphic(canvas);
        }

        ship.drawGraphic(canvas);
    }
}


