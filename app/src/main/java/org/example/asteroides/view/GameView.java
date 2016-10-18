package org.example.asteroides.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import org.example.asteroides.Preferences;
import org.example.asteroides.R;
import org.example.asteroides.logic.GraphicGame;

import java.util.List;
import java.util.Vector;

/**
 * Created by jamarfal on 4/10/16.
 */

public class GameView extends View implements SensorEventListener

{
    // //// ASTEROIDES //////
    private Vector<GraphicGame> asteroids; // Vector con los Asteroides
    private int numAsteroids = 5; // Número inicial de asteroids
    private int numFragments;// Fragmentos en que se divide
    private GraphicGame ship;
    private int turnShip; // Incremento de dirección
    private double shipAcceleration; // aumento de velocidad
    private static final int MAX_SHIP_VELOCITY = 20;
    // Incremento estándar de giro y aceleración
    private static final int STEP_TURN_SHIP = 5;
    private static final float STEP_ACCELERATION_SHIP = 0.5f;
    // //// THREAD Y TIEMPO //////
    // Thread encargado de procesar el juego
    private GameThread gameThread = new GameThread();
    // Cada cuanto queremos procesar cambios (ms)
    private static int PROCESS_PERIOD = 50;
    // Cuando se realizó el último proceso
    private long lastProcessTime = 0;
    private float mX = 0, mY = 0;
    private boolean shooting = false;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable drawableShip, drawableAsteroid, drawableMisil;
        SharedPreferences pref = PreferenceManager.
                getDefaultSharedPreferences(getContext());

        numFragments = tryParseInt(pref.getString(Preferences.KEY_GRAPH, "1"));

        if (playerHasSelectedVectorial(pref)) {
            drawableAsteroid = drawPathForAsteroid();
            drawableShip = drawPathForShip();
            setBackgroundColor(Color.BLACK);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } else {
            drawableAsteroid = ContextCompat.getDrawable(context, R.drawable.asteroide1);
            drawableShip = ContextCompat.getDrawable(context, R.drawable.nave);
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        initOrientationSensor(context);

        initGraphics(drawableShip, drawableAsteroid);
    }


    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anter,
                                 int alto_anter) {
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

        // Posiciona la nave en el centro de la vista
        ship.setCenX(ancho / 2);
        ship.setCenY(alto / 2);

        // Una vez que conocemos nuestro ancho y alto.
        for (GraphicGame asteroide : asteroids) {
            do {
                asteroide.setCenX((int) (Math.random() * ancho));
                asteroide.setCenY((int) (Math.random() * alto));
            } while (asteroide.distance(ship) < (ancho + alto) / 5);
        }

        lastProcessTime = System.currentTimeMillis();
        gameThread.start();


    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (GraphicGame asteroide : asteroids) {
            asteroide.drawGraphic(canvas);
        }

        ship.drawGraphic(canvas);
    }

    private void initOrientationSensor(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        if (!sensorList.isEmpty()) {
            Sensor orientationSensor = sensorList.get(0);
            sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    private void initGraphics(Drawable drawableShip, Drawable drawableAsteroid) {
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

    private boolean playerHasSelectedVectorial(SharedPreferences pref) {
        return pref.getString(Preferences.KEY_GRAPH, "1").equals("0");
    }

    private Drawable drawPathForShip() {
        Drawable drawableShip;
        Path pathShip = new Path();
        pathShip.moveTo(0.0f, 0.0f);
        pathShip.lineTo(0.0f, 1.0f);
        pathShip.lineTo(1.0f, .5f);
        pathShip.lineTo(0.0f, 0.0f);
        ShapeDrawable shapeDrawableShip = new ShapeDrawable(new PathShape(pathShip, 1, 1));
        shapeDrawableShip.getPaint().setColor(Color.WHITE);
        shapeDrawableShip.getPaint().setStyle(Paint.Style.STROKE);
        shapeDrawableShip.setIntrinsicWidth(20);
        shapeDrawableShip.setIntrinsicHeight(15);
        drawableShip = shapeDrawableShip;
        return drawableShip;
    }

    private Drawable drawPathForAsteroid() {
        Drawable drawableAsteroid;
        Path pathAsteroide = new Path();
        pathAsteroide.moveTo((float) 0.3, (float) 0.0);
        pathAsteroide.lineTo((float) 0.6, (float) 0.0);
        pathAsteroide.lineTo((float) 0.6, (float) 0.3);
        pathAsteroide.lineTo((float) 0.8, (float) 0.2);
        pathAsteroide.lineTo((float) 1.0, (float) 0.4);
        pathAsteroide.lineTo((float) 0.8, (float) 0.6);
        pathAsteroide.lineTo((float) 0.9, (float) 0.9);
        pathAsteroide.lineTo((float) 0.8, (float) 1.0);
        pathAsteroide.lineTo((float) 0.4, (float) 1.0);
        pathAsteroide.lineTo((float) 0.0, (float) 0.6);
        pathAsteroide.lineTo((float) 0.0, (float) 0.2);
        pathAsteroide.lineTo((float) 0.3, (float) 0.0);
        ShapeDrawable dAsteroide = new ShapeDrawable(
                new PathShape(pathAsteroide, 1, 1));
        dAsteroide.getPaint().setColor(Color.WHITE);
        dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
        dAsteroide.setIntrinsicWidth(50);
        dAsteroide.setIntrinsicHeight(50);
        drawableAsteroid = dAsteroide;
        return drawableAsteroid;
    }


    private int tryParseInt(String numberStr) {
        int number;
        try {
            number = Integer.parseInt(numberStr);
        } catch (NumberFormatException exception) {
            number = 0;
        }
        return number;
    }

    protected synchronized void updatePhysics() {

        long now = System.currentTimeMillis();

        // Salir si el período de proceso no se ha cumplido.
        if (lastProcessTime + PROCESS_PERIOD > now) {
            return;
        }

        // Para una ejecución en tiempo real calculamos retardo
        double retardation = calculateRetardation(now);

        lastProcessTime = now; // Para la próxima vez

        // Actualizamos velocidad y dirección de la nave a partir de
        // giroNave y aceleracionNave (según la entrada del jugador)
        updateShipVelocityAndDirection(retardation);


        updatePositions(retardation);
    }

    private void updateShipVelocityAndDirection(double retardation) {

        ship.setAngle((int) (ship.getAngle() + turnShip * retardation));

        double nIncX = ship.getIncX() + shipAcceleration *
                Math.cos(Math.toRadians(ship.getAngle())) * retardation;
        double nIncY = ship.getIncY() + shipAcceleration *
                Math.sin(Math.toRadians(ship.getAngle())) * retardation;

        // Actualizamos si el módulo de la velocidad no excede el máximo
        if (Math.hypot(nIncX, nIncY) <= MAX_SHIP_VELOCITY) {
            ship.setIncX(nIncX);
            ship.setIncY(nIncY);
        }
    }

    private void updatePositions(double retardation) {
        ship.increasePosition(retardation); // Actualizamos posición
        for (GraphicGame asteroide : asteroids) {
            asteroide.increasePosition(retardation);
        }
    }

    private double calculateRetardation(long now) {
        return (now - lastProcessTime) / PROCESS_PERIOD;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        // Suponemos que vamos a procesar la pulsación
        boolean procesada = true;
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                shipAcceleration = 0;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                turnShip = 0;
                break;
            default:
                // Si estamos aquí, no hay pulsación que nos interese
                procesada = false;
                break;
        }
        return procesada;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                shooting = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dy < 6 && dx > 6) {
                    turnShip = Math.round((x - mX) / 2);
                    shooting = false;
                } else if (dx < 6 && dy > 6) {
                    if (y < mY)
                        shipAcceleration = Math.round((mY - y) / 25);
                    shooting = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                turnShip = 0;
                shipAcceleration = 0;
                if (shooting) {
                    //activaMisil();
                }
                break;
        }
        mX = x;
        mY = y;
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class GameThread extends Thread {

        @Override
        public void run() {
            while (true) {
                updatePhysics();
            }
        }
    }
}


