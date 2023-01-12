package com.engineandroid;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class LightSensor implements com.engineandroid.Sensor{
    Context context;
    SensorManager sensorManager;
    android.hardware.Sensor sensor;
    SensorEventListener lightSensor;

    int lastValues;
    int deltaValues;


    public LightSensor(Context context){
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_LIGHT);

        //No se hace nada si no tiene acelerómetro el dispositivo
        if(sensor == null)
            return;

        lightSensor = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                //Guardamos intensidad del sensor
                deltaValues = (int) Math.abs(lastValues - event.values[0]);
                lastValues = (int) event.values[0];
            }

            @Override
            public void onAccuracyChanged(android.hardware.Sensor sensor, int i) {

            }
        };

        onResume();
    }

    @Override
    public void onResume() {
        if(lightSensor != null)
            sensorManager.registerListener(lightSensor, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        if(lightSensor != null)
            sensorManager.unregisterListener(lightSensor);
    }

    public int getDeltaValues(){
        return deltaValues;
    }

    public int getActualValues(){
        return lastValues;
    }
}
