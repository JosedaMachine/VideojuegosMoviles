package com.engineandroid;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class GyroscopeSensor implements com.engineandroid.Sensor {

    Context context;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener gyroscope;

    float[] lastValues;

    public GyroscopeSensor(Context context){
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        //No se hace nada si no tiene giroscopio el dispositivo
        if(sensor == null)
            return;

        gyroscope = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                lastValues = event.values.clone();
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

    public float[] getActualValues(){
        return lastValues;
    }

}
