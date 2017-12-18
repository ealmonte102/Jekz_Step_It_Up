package com.jekz.stepitup;

import android.app.Application;
import android.hardware.SensorManager;

import com.jekz.stepitup.model.step.ManualStepCounter;

/**
 * Created by evanalmonte on 12/11/17.
 */

public class JekzApplication extends Application {
    ManualStepCounter stepCounter;

    public JekzApplication() {
        super();
    }

    @Override
    public void onCreate() {
        SensorManager manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounter = new ManualStepCounter(manager);
        stepCounter.registerSensor();
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        stepCounter.unregisterSensor();
    }

    public ManualStepCounter getStepCounter() {
        return stepCounter;
    }
}
