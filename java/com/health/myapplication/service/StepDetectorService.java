package com.health.myapplication.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.health.myapplication.callback.stepsCallback;
import com.health.myapplication.helper.GeneralHelper;
import com.health.myapplication.helper.PrefsHelper;

import java.util.Arrays;

public class StepDetectorService extends Service implements SensorEventListener {

    private static stepsCallback callback;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (countSensor != null) {
            Toast.makeText(this, "Step Detecting Start", Toast.LENGTH_SHORT).show();
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);

            GeneralHelper.updateNotification(this, this, PrefsHelper.getInt("FSteps"));
            if (callback != null) {
                callback.subscribeSteps(PrefsHelper.getInt("FSteps"));
            }
        } else {
            Toast.makeText(this, "Sensor Not Detected", Toast.LENGTH_SHORT).show();
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("StepDetectorService", "onSensorChanged: " + Arrays.toString(event.values));
        if (event.values.length > 0) {
            if (!PrefsHelper.getString("TodayDate").equals(GeneralHelper.getTodayDate())) {
                PrefsHelper.putInt("Steps", Math.round(event.values[0]));
                PrefsHelper.putString("TodayDate", GeneralHelper.getTodayDate());
            } else {
                int storeSteps = PrefsHelper.getInt("Steps");
                int sensorSteps = Math.round(event.values[0]);
                int finalSteps = sensorSteps - storeSteps;
                if (finalSteps > 0) {
                    PrefsHelper.putInt("FSteps", finalSteps);
                    GeneralHelper.updateNotification(this, this, finalSteps);
                    if (callback != null) {
                        callback.subscribeSteps(finalSteps);
                    }
                }
            }
        } else {
            Log.e("StepDetectorService", "Invalid sensor data");
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("SERVICE", sensor.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(this);
    }

    public static class subscribe {
        public static void register(stepsCallback stepsCallback) {
            callback = stepsCallback;
        }
    }
}

