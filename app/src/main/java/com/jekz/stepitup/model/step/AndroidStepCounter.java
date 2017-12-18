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
    private static final String TAG = AndroidStepCounter.class.getName();

    private SensorManager manager;
    private Sensor stepSensor;

    private long intervalInMillis;
    private ArrayList<StepCounterCallback> listeners = new ArrayList<>();

    private int sessionStartedSteps;
    private int baseSteps;
    private int mostRecentSteps;
    private long sessionStartTime;

    private Handler handler = new Handler();
    private boolean autoCounting;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            notifySessionEnded(new Session(sessionStartTime, System.currentTimeMillis(),
                    mostRecentSteps - sessionStartedSteps));
            resetCount();
            handler.postDelayed(runnable, intervalInMillis);
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
    public synchronized void registerSensor() {
        Log.i("StepCounter", "Sensor is now registered");
        manager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public synchronized void unregisterSensor() {
        Log.i("StepCounter", "Sensor is now UN-registered");
        handler.removeCallbacks(runnable);
        manager.unregisterListener(this);
    }

    @Override
    public void startSession() {

    }

    @Override
    public void endSession() {

    }

    private synchronized void resetCount() {
        sessionStartedSteps = mostRecentSteps;
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
        handler.removeCallbacks(runnable);
        notifySessionEnded(session);
    }

    @Override
    public void notifySessionEnded(Session session) {
        Log.d(TAG, "Session ended: " + session.toString());
        for (StepCounterCallback listener : listeners) {
            //listener.onSessionEnded(session);
        }
    }

    @Override
    public synchronized void startAutoCount() {
        if (!autoCounting) {
            Log.d(TAG, "Starting auto-count");
            resetCount();
            autoCounting = true;
            handler.postDelayed(runnable, intervalInMillis);
        }
    }

    @Override
    public void stopAutoCount() {
        if (autoCounting) {
            Log.d(TAG, "Stopping auto-count");
            notifySessionEnded(new Session(sessionStartTime, System.currentTimeMillis(),
                    mostRecentSteps - sessionStartedSteps));
            resetCount();
            autoCounting = false;
            handler.removeCallbacks(runnable);
        }
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
            notiftyListeners(mostRecentSteps - sessionStartedSteps);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}