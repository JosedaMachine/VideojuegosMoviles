package com.engineandroid;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MagnetometerSensor implements Sensor{

    SensorManager sensorManager;
    android.hardware.Sensor sensor;
    SensorEventListener magnetometer;

    float[] deltaValues;

    public final int NUM_VALUES = 3;

    public MagnetometerSensor(Context context){
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_MAGNETIC_FIELD);

        deltaValues = new float[NUM_VALUES];
        //No se hace nada si no tiene giroscopio el dispositivo
        if(sensor == null)
            return;

        magnetometer = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                deltaValues = event.values.clone();
            }

            @Override
            public void onAccuracyChanged(android.hardware.Sensor sensor, int i) {

            }
        };

        onResume();
    }

    @Override
    public void onResume() {
        if(magnetometer != null)
            sensorManager.registerListener(magnetometer, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        if(magnetometer != null)
            sensorManager.unregisterListener(magnetometer);
    }

    public float[] getDeltaValues(){
        return deltaValues;
    }
}
