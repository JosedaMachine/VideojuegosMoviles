package com.engineandroid;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class GyroscopeSensor implements com.engineandroid.Sensor {

    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener gyroscope;

    float[] deltaValues;

    public final int NUM_VALUES = 3;

    public GyroscopeSensor(Context context){
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        deltaValues = new float[NUM_VALUES];
        //No se hace nada si no tiene giroscopio el dispositivo
        if(sensor == null)
            return;

        gyroscope = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                deltaValues = event.values.clone();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        onResume();
    }

    @Override
    public void onResume() {
        if(gyroscope != null)
            sensorManager.registerListener(gyroscope, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        if(gyroscope != null)
            sensorManager.unregisterListener(gyroscope);
    }

    public float[] getDeltaValues(){
        return deltaValues;
    }

}
