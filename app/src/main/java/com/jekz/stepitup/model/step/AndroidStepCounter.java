package com.jekz.stepitup.model.step;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by evanalmonte on 12/8/17.
 */

public class AndroidStepCounter implements StepCounter, SensorEventListener {
    private static AndroidStepCounter instance;
    private SensorManager manager;
    private Sensor stepSensor;
    private ArrayList<StepCounterCallback> listeners = new ArrayList<>();
    private int sessionStartedSteps;
    private int mostRecentSteps;
    private long sessionStartTime;

    private AndroidStepCounter(SensorManager manager) {
        this.manager = manager;
        stepSensor = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    public static AndroidStepCounter getInstance(SensorManager sensorManager) {
        if (instance == null) {
            instance = new AndroidStepCounter(sensorManager);
        }
        return instance;
    }

    @Override
    public void addListener(StepCounterCallback listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(StepCounterCallback listener) {
        listeners.remove(listener);
    }

    @Override
    public void registerSensor() {
        Log.d("Register Sensor", "Sensor is now registered");
        resetCount();
        manager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public Session unregisterSensor() {
        manager.unregisterListener(this);
        return new Session(sessionStartTime, System.currentTimeMillis(),
                mostRecentSteps - sessionStartedSteps);
    }

    private void resetCount() {
        sessionStartedSteps = 0;
        mostRecentSteps = 0;
        sessionStartTime = System.currentTimeMillis();
    }

    @Override
    public void notiftyListeners(int numOfSteps) {
        for (StepCounterCallback listener : listeners) {
            listener.onStepDetected(numOfSteps);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (sessionStartedSteps < 1) {
                sessionStartedSteps = (int) sensorEvent.values[0];
            }
            mostRecentSteps = (int) sensorEvent.values[0];
            notiftyListeners(mostRecentSteps - sessionStartedSteps);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}