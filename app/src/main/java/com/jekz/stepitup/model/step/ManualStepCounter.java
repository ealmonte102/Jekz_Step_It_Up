package com.jekz.stepitup.model.step;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by evanalmonte on 12/18/17.
 */

public class ManualStepCounter implements SessionStepCounter, SensorEventListener {
    private static final String TAG = AndroidStepCounter.class.getName();
    boolean sessionActive;
    private SensorManager manager;
    private Sensor stepSensor;
    private ArrayList<SessionListener> listeners = new ArrayList<>();
    private int baseSteps;
    private int sessionStartedSteps;
    private int mostRecentSteps;
    private long sessionStartTime;

    public ManualStepCounter(SensorManager manager) {
        this.manager = manager;
        stepSensor = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    public void addSessionListener(SessionListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeSessionListener(SessionListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void registerSensor() {
        Log.i("StepCounter", "Sensor is now registered");
        manager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void unregisterSensor() {
        Log.i("StepCounter", "Sensor is now UN-registered");
        manager.unregisterListener(this);
    }

    @Override
    public void startSession() {
        if (sessionActive) { return; }
        sessionActive = true;
        sessionStartedSteps = baseSteps;
        mostRecentSteps = baseSteps;
        sessionStartTime = System.currentTimeMillis();
    }

    @Override
    public void endSession() {
        if (!sessionActive) { return; }
        notifySessionListeners(new Session(sessionStartTime, System.currentTimeMillis(),
                mostRecentSteps -
                sessionStartedSteps));
        sessionActive = false;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (baseSteps < 1) {
                baseSteps = (int) sensorEvent.values[0];
                mostRecentSteps = baseSteps;
                sessionStartedSteps = baseSteps;
            }
            mostRecentSteps = (int) sensorEvent.values[0];
            Log.d("Steps", String.valueOf(mostRecentSteps));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void notifySessionListeners(Session session) {
        Log.d("Manual session counter", "Session ended: " + session.toString());
        for (SessionListener listener : listeners) {
            listener.sessionEnded(session);
        }
    }
}
