package com.jekz.stepitup;

import android.app.Application;
import android.hardware.SensorManager;

import com.jekz.stepitup.model.step.AndroidStepCounter;
import com.jekz.stepitup.model.step.IntervalStepCounter;

import java.util.concurrent.TimeUnit;

/**
 * Created by evanalmonte on 12/11/17.
 */

public class JekzApplication extends Application {
    IntervalStepCounter stepCounter;

    public JekzApplication() {
        super();
    }

    @Override
    public void onCreate() {
        SensorManager manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounter = new AndroidStepCounter(manager, TimeUnit.SECONDS.toMillis(120));
        stepCounter.registerSensor();
        stepCounter.startAutoCount();
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        stepCounter.unregisterSensor();
    }

    public IntervalStepCounter getStepCounter() {
        return stepCounter;
    }
}
