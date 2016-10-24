package org.example.asteroides.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

/**
 * Created by jamarfal on 24/10/16.
 */

public class SensorController {

    private SensorManager sensorManager;
    private Sensor orientationSensor;
    private Sensor accelerometerSensor;
    private SensorEventListener sensorEventListener;


    public SensorController(Context context, SensorEventListener sensorEventListener) {
        initSensor(context, sensorEventListener);
    }

    private void initSensor(Context context, SensorEventListener sensorEventListener) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.sensorEventListener = sensorEventListener;
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (!sensorList.isEmpty()) {
            accelerometerSensor = sensorList.get(0);
            activateSensor();
        }
    }

    public void activateSensor() {
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void deactivateSensor() {
        sensorManager.unregisterListener(sensorEventListener);
    }
}
