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
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import org.example.asteroides.Preferences;
import org.example.asteroides.R;
import org.example.asteroides.pool.FxSoundPool;
import org.example.asteroides.pool.PoolObject;

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
    /////// SHIP //////
    private Ship ship;

    // //// THREAD Y TIEMPO //////
    // Thread encargado de procesar el juego
    private GameThread gameThread = new GameThread();
    // Cada cuanto queremos procesar cambios (ms)
    private static int PROCESS_PERIOD = 50;
    // Cuando se realizó el último proceso
    private long lastProcessTime = 0;
    private float mX = 0, mY = 0;
    private boolean shooting = false;
    // //// MISIL //////
//    private Misil misil;
//    private Vector<Misil> misiles;
    private PoolObject misilPool;
    private Misil currentMisil;
    private SensorManager sensorManager;
    private Sensor orientationSensor;
    private Context context;

    //region Constructor
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Drawable drawableShip, drawableAsteroid, drawableMisil;
        SharedPreferences pref = PreferenceManager.
                getDefaultSharedPreferences(getContext());


        numFragments = tryParseInt(pref.getString(Preferences.KEY_GRAPH, "1"));

        if (playerHasSelectedVectorial(pref)) {
            drawableAsteroid = drawPathForAsteroid();
            drawableShip = drawPathForShip();
            drawableMisil = drawPathForMisile();
            setBackgroundColor(Color.BLACK);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } else {
            drawableAsteroid = ContextCompat.getDrawable(context, R.drawable.asteroide1);
            drawableShip = ContextCompat.getDrawable(context, R.drawable.nave);
            drawableMisil = ContextCompat.getDrawable(context, R.drawable.misil1);
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        if (playerHasSelectedSensorControl(pref)) {
            initSensor(context);
        }


        initGraphics(drawableShip, drawableAsteroid, drawableMisil);

    }
    //endregion


    public GameThread getGameThread() {
        return gameThread;
    }

    //region View Methods
    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anter,
                                 int alto_anter) {
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

        // Posiciona la nave en el centro de la vista
        ship.positionIn(ancho / 2, alto / 2);

        // Una vez que conocemos nuestro ancho y alto.
        for (GraphicGame asteroide : asteroids) {
            do {
                asteroide.setCenX((int) (Math.random() * ancho));
                asteroide.setCenY((int) (Math.random() * alto));
            } while (asteroide.distance(ship.getGraphicGame()) < (ancho + alto) / 5);
        }

        lastProcessTime = System.currentTimeMillis();
        gameThread.start();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ship.draw(canvas);

        synchronized (asteroids) {
            for (GraphicGame asteroide : asteroids) {
                asteroide.drawGraphic(canvas);
            }
        }

        for (Misil misil : misilPool.getObjects()) {
            if (misil.isActive())
                misil.draw(canvas);
        }
    }
    //endregion

    //region Accesory Init Methods
    private void initSensor(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (!sensorList.isEmpty()) {
            orientationSensor = sensorList.get(0);
            activateSensor();
        }
    }

    public void activateSensor() {
        sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void deactivateSensor() {
        sensorManager.unregisterListener(this);
    }

    private void initGraphics(Drawable drawableShip, Drawable drawableAsteroid, Drawable drawableMisile) {
        GraphicGame graphicGameShip = new GraphicGame(this, drawableShip);
        ship = new Ship(graphicGameShip);
//        misil = new Misil(new GraphicGame(this, drawableMisile), ship);
//        misiles = new Vector<>();
        misilPool = new PoolObject();
        asteroids = new Vector<GraphicGame>();

        for (int i = 0; i < numAsteroids; i++) {
            GraphicGame asteroid = new GraphicGame(this, drawableAsteroid);
            asteroid.setIncY(Math.random() * 4 - 2);
            asteroid.setIncX(Math.random() * 4 - 2);
            asteroid.setAngle((int) (Math.random() * 360));
            asteroid.setRotacion((int) (Math.random() * 8 - 4));
            asteroids.add(asteroid);
        }

//        for (int i = 0; i < 5; i++) {
//            Misil misil = new Misil(new GraphicGame(this, drawableMisile), ship);
//            misiles.add(misil);
//        }
    }


    private boolean playerHasSelectedVectorial(SharedPreferences pref) {
        return pref.getString(Preferences.KEY_GRAPH, "1").equals("0");
    }

    private boolean playerHasSelectedSensorControl(SharedPreferences pref) {
        return pref.getBoolean(Preferences.KEY_SENSOR, true);
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

    private ShapeDrawable drawPathForMisile() {
        ShapeDrawable dMisil = new ShapeDrawable(new RectShape());
        dMisil.getPaint().setColor(Color.WHITE);
        dMisil.getPaint().setStyle(Paint.Style.STROKE);
        dMisil.setIntrinsicWidth(15);
        dMisil.setIntrinsicHeight(3);
        return dMisil;
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
    //endregion

    //region Physics Methods
    protected void updatePhysics() {

        long now = System.currentTimeMillis();

        // Salir si el período de proceso no se ha cumplido.
        if (lastProcessTime + PROCESS_PERIOD > now) {
            return;
        }

        // Para una ejecución en tiempo real calculamos retardo
        double retardation = calculateRetardation(now);

        lastProcessTime = now; // Para la próxima vez


        updatePositions(retardation);
    }


    private void updatePositions(double retardation) {
        ship.move(retardation);

        for (GraphicGame asteroide : asteroids) {
            asteroide.increasePosition(retardation);
        }

        // Actualizamos posición de misil
//        if (misil.isActive()) {
//            misil.updatePositions(retardation);
//            int timeMisil = misil.getTimeMisil();
//            timeMisil -= retardation;
//            misil.setTimeMisil(timeMisil);
//            if (misil.getTimeMisil() < 0) {
//                misil.setActive(false);
//            } else {
//                for (int i = 0; i < asteroids.size(); i++)
//                    if (misil.checkCollision(asteroids.elementAt(i))) {
//                        destroyAsteroid(i);
//                        break;
//                    }
//            }
//        }
//        for (Misil misil : misiles) {
//            if (misil.isActive()) {
//                misil.updatePositions(retardation);
//                int timeMisil = misil.getTimeMisil();
//                timeMisil -= retardation;
//                misil.setTimeMisil(timeMisil);
//                if (misil.getTimeMisil() < 0) {
//                    misil.setActive(false);
//                } else {
//                    for (int i = 0; i < asteroids.size(); i++)
//                        if (misil.checkCollision(asteroids.elementAt(i))) {
//                            destroyAsteroid(i);
//                            break;
//                        }
//                }
//            }
//        }

        for (Misil misil : misilPool.getObjects()) {
            if (misil.isActive()) {
                misil.updatePositions(retardation);
                int timeMisil = misil.getTimeMisil();
                timeMisil -= retardation;
                misil.setTimeMisil(timeMisil);
                if (misil.getTimeMisil() < 0) {
                    misil.setActive(false);
                } else {
                    for (int i = 0; i < asteroids.size(); i++)
                        if (misil.checkCollision(asteroids.elementAt(i))) {
                            destroyAsteroid(i);
                            break;
                        }
                }
            }
        }
    }

    private void destroyAsteroid(int i) {
        FxSoundPool.getInstance(context).explossion();
        synchronized (asteroids) {
            asteroids.remove(i);
        }
    }

    private double calculateRetardation(long now) {
        return (now - lastProcessTime) / PROCESS_PERIOD;
    }
    //endregion

    //region Keyboard Input
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        // Suponemos que vamos a procesar la pulsación
        boolean procesada = true;
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                ship.setShipAcceleration(0);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                ship.setTurnShip(0);
                break;
            default:
                // Si estamos aquí, no hay pulsación que nos interese
                procesada = false;
                break;
        }
        return procesada;
    }
    //endregion

    //region Touch Input
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
                    ship.setTurnShip(Math.round((x - mX) / 2));
                    shooting = false;
                } else if (dx < 6 && dy > 6) {
                    if (y < mY)
                        ship.setShipAcceleration(Math.round((mY - y) / 25));
                    shooting = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                ship.setTurnShip(0);
                ship.setShipAcceleration(0);
                if (shooting) {
//                    for (Misil misil : misiles) {
//                        if (!misil.isActive()) {
//                            misil.fire();
//                            int timeMisil = (int) Math.min(this.getWidth() / Math.abs(misil.getGraphicGame().
//                                    getIncX()), this.getHeight() / Math.abs(misil.getGraphicGame().getIncY())) - 2;
//                            misil.setTimeMisil(timeMisil);
//                            misil.setActive(true);
//                            break;
//                        }
//                    }
                    currentMisil = misilPool.get(new GraphicGame(this, ContextCompat.getDrawable(getContext(), R.drawable.misil1)), ship.getGraphicGame(), context);
                    if (!currentMisil.isActive()) {
                        currentMisil.fire();
                        int timeMisil = (int) Math.min(this.getWidth() / Math.abs(currentMisil.getGraphicGame().
                                getIncX()), this.getHeight() / Math.abs(currentMisil.getGraphicGame().getIncY())) - 2;
                        currentMisil.setTimeMisil(timeMisil);
                        currentMisil.setActive(true);
                        break;
                    }

                }
                break;
        }
        mX = x;
        mY = y;
        return true;
    }
    //endregion

    //region Sensor Input
    private float[] lastAcceloremeterValues;
    static final float ALPHA = 0.3f; // if ALPHA = 1 OR 0, no filter applies.

    @Override
    public void onSensorChanged(SensorEvent event) {
        float xValue;
        float yValue;
        lastAcceloremeterValues = highPass(event.values.clone(), lastAcceloremeterValues);

        xValue = lastAcceloremeterValues[0];
        yValue = lastAcceloremeterValues[1];
//        turnShip = (int) yValue * STEP_TURN_SHIP;
//        shipAcceleration = (int) xValue * STEP_ACCELERATION_SHIP;
        ship.setTurnShip((int) yValue);
        ship.setShipAcceleration((int) xValue);
    }

    private float[] highPass(float[] input, float[] output) {
        if (output == null) return input;
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i] - (ALPHA * output[i] + (1 - ALPHA) * input[i]);
        }
        return output;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    //endregion

    //region Inner Class
    public class GameThread extends Thread {
        private boolean pause, running;

        public synchronized void pauseThread() {
            pause = true;
        }

        public synchronized void resumeThread() {
            pause = false;
            notify();
        }

        public void stopThread() {
            running = false;
            if (pause) resumeThread();
        }

        @Override
        public void run() {
            running = true;
            while (running) {
                updatePhysics();
                synchronized (this) {
                    while (pause) {
                        try {
                            wait();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }
    //endregion


}


