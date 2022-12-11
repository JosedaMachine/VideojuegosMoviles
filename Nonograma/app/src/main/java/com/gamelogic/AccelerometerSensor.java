package com.gamelogic;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

//TODO esto debe ir en el motor, no la logica?
public class AccelerometerSensor {
    Context context;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener accelerometer;

    float[] lastValues;
    float[] deltaValues;

    public AccelerometerSensor(Context context){
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        lastValues = new float[] {0,0,0};
        deltaValues = new float[] {0,0,0};

        //TODO protección contra móviles sin acelerómetro
        //if(sensor == null) NO SE PUEDE USAR LA CLASE

        accelerometer = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                for(int i = 0; i < 3; i++){
                    //Guardamos movimiento del acelerómetro
                    deltaValues[i] = Math.abs(lastValues[i] - event.values[i]);
                    //Si el movimiento es muy pequeño se desprecia, es ruido
                    if(deltaValues[i] < 2) deltaValues[i] = 0;
                    //Se guarda el nuevo valor
                    lastValues[i] = event.values[i];
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        onResume();
    }

    //Métodos que llamar para que no consuma la aplicación mientras que está cerrada
    //El sensor SIGUE ACTIVO si no se quita del registro aunque se minimice la app
    public void onResume() {
        sensorManager.registerListener(accelerometer, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause() {
        sensorManager.unregisterListener(accelerometer);
    }

    public float[] getDeltaValues(){
        return deltaValues;
    }

    public float[] getActualValues(){
        return lastValues;
    }
}
