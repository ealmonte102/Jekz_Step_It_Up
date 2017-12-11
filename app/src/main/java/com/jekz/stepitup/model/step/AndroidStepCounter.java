package com.jekz.stepitup.model.step;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by evanalmonte on 12/8/17.
 */

public class AndroidStepCounter implements IntervalStepCounter, SensorEventListener {
    private static AndroidStepCounter instance;
    private SensorManager manager;
    private Sensor stepSensor;

    private long intervalInMillis;
    private ArrayList<StepCounterCallback> listeners = new ArrayList<>();

    private int sessionStartedSteps;
    private int mostRecentSteps;
    private long sessionStartTime;

    private Handler handler = new Handler();
    private boolean autoCounting = false;
    private Runnable autoCount = new Runnable() {
        @Override
        public void run() {
            unregisterSensor();
            startAutoCount();
        }
    };

    public AndroidStepCounter(SensorManager manager, long intervalInMillis) {
        this.manager = manager;
        this.intervalInMillis = intervalInMillis;
        stepSensor = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
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
        if (!autoCounting) {
            Log.i("StepCounter", "Sensor is now registered");
            resetCount();
            manager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public synchronized void unregisterSensor() {
        Log.i("StepCounter", "Sensor is now UN-registered");
        autoCounting = false;
        handler.removeCallbacks(autoCount);
        manager.unregisterListener(this);
        Session session = new Session(sessionStartTime, System.currentTimeMillis(),
                mostRecentSteps - sessionStartedSteps);
        notifySessionEnded(session);
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
    public synchronized void setInterval(long intervalInMillis) {
        this.intervalInMillis = intervalInMillis;
        Session session = new Session(sessionStartTime, System.currentTimeMillis(),
                mostRecentSteps - sessionStartedSteps);
        notifySessionEnded(session);
        handler.removeCallbacks(autoCount);
        startAutoCount();
    }

    @Override
    public void notifySessionEnded(Session session) {
        for (StepCounterCallback listener : listeners) {
            listener.onSessionEnded(session);
        }
    }

    @Override
    public synchronized void startAutoCount() {
        if (!autoCounting) {
            registerSensor();
            handler.postDelayed(autoCount, intervalInMillis);
            autoCounting = true;
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