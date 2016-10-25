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
    private Sensor sensor;
    private SensorEventListener sensorEventListener;


    public SensorController(Context context, SensorEventListener sensorEventListener, int sensorType) {
        initSensor(context, sensorEventListener, sensorType);
    }

    private void initSensor(Context context, SensorEventListener sensorEventListener, int sensorType) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.sensorEventListener = sensorEventListener;
        List<Sensor> sensorList = sensorManager.getSensorList(sensorType);
        if (!sensorList.isEmpty()) {
            sensor = sensorList.get(0);
            activateSensor();
        }
    }

    public void activateSensor() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void deactivateSensor() {
        sensorManager.unregisterListener(sensorEventListener);
    }
}
