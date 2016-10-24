package org.example.asteroides.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import org.example.asteroides.pool.FxSoundPool;
import org.example.asteroides.pool.MisilPool;
import org.example.asteroides.preferences.GamePreferences;
import org.example.asteroides.sensor.SensorController;

import java.util.Iterator;
import java.util.Vector;

/**
 * Created by jamarfal on 4/10/16.
 */

public class GameView extends View implements SensorEventListener

{
    // //// ASTEROIDES //////
    private Vector<Asteroid> asteroids; // Vector con los Asteroides
    private int numAsteroids = 5; // Número inicial de asteroids
    /////// SHIP //////
    private Ship ship;
    ////// THREAD Y TIEMPO //////
    private GameThread gameThread = new GameThread();
    private static int PROCESS_PERIOD = 50;    // Cada cuanto queremos procesar cambios (ms)
    private long lastProcessTime = 0;// Cuando se realizó el último proceso
    private float mX = 0, mY = 0;
    private boolean shooting = false;
    ////// MISIL //////
    private MisilPool misilPool;
    private Misil currentMisil;
    private Context context;
    ////// SENSOR //////
    private SensorController sensorController;
    /// PREFERENCES ////
    private GamePreferences gamePreferences;
    /// DRAWABLECONTROLLER ///
    private DrawableController drawableController;
    ////// DRAWABLES //////
    private Drawable drawableShip, drawableAsteroid, drawableMisil;

    //region Constructor
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        gamePreferences = new GamePreferences(context);
        drawableController = new DrawableController(context);

        if (gamePreferences.playerHasSelectedVectorial()) {
            drawableAsteroid = drawableController.drawPathForAsteroid();
            drawableShip = drawableController.drawPathForShip();
            drawableMisil = drawableController.drawPathForMisile();
            setBackgroundColor(Color.BLACK);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } else {
            drawableAsteroid = drawableController.getAsteroid();
            drawableShip = drawableController.getShip();
            drawableMisil = drawableController.getMisil();
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        initGameObjects();

        if (gamePreferences.playerHasSelectedSensorControl()) {
            sensorController = new SensorController(context, this);
        }


    }
    //endregion

    //region Accesory Init Methods
    private void initGameObjects() {
        GraphicGame graphicGameShip = new GraphicGame(this, drawableShip);
        ship = new Ship(graphicGameShip);

        asteroids = new Vector<>();

        for (int i = 0; i < numAsteroids; i++) {
            GraphicGame graphicGameAsteroid = new GraphicGame(this, drawableAsteroid);
            Asteroid asteroid = new Asteroid(graphicGameAsteroid, gamePreferences.getNumFragments());
            asteroid.setVelocity((int) (Math.random() * 4 - 2), (int) (Math.random() * 4 - 2));
            asteroid.setAngle((int) (Math.random() * 360));
            asteroid.setRotacion((int) (Math.random() * 8 - 4));
            asteroids.add(asteroid);
        }

        misilPool = new MisilPool();
    }
    //endregion

    //region Getters
    public GameThread getGameThread() {
        return gameThread;
    }

    public SensorController getSensorController() {
        return sensorController;
    }
    //endregion

    //region View Methods
    @Override
    protected void onSizeChanged(int width, int height, int previousWidth, int previousHeight) {
        super.onSizeChanged(width, height, previousWidth, previousHeight);

        // Posiciona la nave en el centro de la vista
        ship.positionIn(width / 2, height / 2);

        // Una vez que conocemos nuestro ancho y alto.
        for (Asteroid asteroid : asteroids) {
            do {
                asteroid.positionIn((int) (Math.random() * width), (int) (Math.random() * height));
            } while (asteroid.distance(ship.getGraphicGame()) < (width + height) / 5);
        }

        lastProcessTime = System.currentTimeMillis();
        gameThread.start();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ship.draw(canvas);

        synchronized (asteroids) {
            for (Asteroid asteroide : asteroids) {
                asteroide.draw(canvas);
            }
        }

        for (int counter = misilPool.getObjects().size() - 1; counter >= 0; counter--) {
            Misil misil = misilPool.getObjects().get(counter);
            if (misil.isActive())
                misil.draw(canvas);
        }
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

        for (Asteroid asteroide : asteroids) {
            asteroide.move(retardation);
        }


        for (int counter = misilPool.getObjects().size() - 1; counter >= 0; counter--) {
            Misil misil = misilPool.getObjects().get(counter);
            if (misil.isActive()) {
                misil.move(retardation);
                if (misil.getTimeMisil() < 0) {
                    misil.setActive(false);
                    misilPool.free(misil);
                } else {
                    for (int i = 0; i < asteroids.size(); i++)
                        if (misil.checkCollision(asteroids.elementAt(i).getGraphicGame())) {
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
                    currentMisil = getMisilFromPool();
                    if (!currentMisil.isActive()) {
                        currentMisil.fire(this.getWidth(), this.getHeight());
                        FxSoundPool.getInstance(getContext()).misil();
                        break;
                    }
                }
                break;
        }
        mX = x;
        mY = y;
        return true;
    }

    private Misil getMisilFromPool() {
        GraphicGame misilGraphic = new GraphicGame(this, drawableMisil);
        return misilPool.get(misilGraphic, ship.getGraphicGame());
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
        ship.setTurnShip((int) yValue * Ship.STEP_TURN_SHIP);
        ship.setShipAcceleration((int) xValue * Ship.STEP_ACCELERATION_SHIP);
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


