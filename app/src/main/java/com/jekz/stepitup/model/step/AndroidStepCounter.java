package com.jekz.stepitup.model.step;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;

/**
 * Created by evanalmonte on 12/8/17.
 */

public class AndroidStepCounter implements StepCounter, SensorEventListener {
    private SensorManager manager;
    private Sensor stepSensor;

    private ArrayList<StepCounterCallback> listeners = new ArrayList<>();

    public AndroidStepCounter(SensorManager manager) {
        this.manager = manager;
        stepSensor = manager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }

    public void addListener(StepCounterCallback listener) {
        listeners.add(listener);
    }

    public void removeListener(StepCounterCallback listener) {
        listeners.remove(listener);
    }

    public void registerSensor() {
        if (stepSensor != null) {
            manager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void unregisterSensor() {
        if (stepSensor != null) {
            manager.unregisterListener(this);
        }
    }

    @Override
    public void notiftyListeners() {
        for (StepCounterCallback listener : listeners) {
            listener.onStepDetected();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if (sensorEvent.values[0] == 1.0) {
                notiftyListeners();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
