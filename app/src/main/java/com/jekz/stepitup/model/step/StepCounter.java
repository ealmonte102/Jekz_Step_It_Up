package com.jekz.stepitup.model.step;

/**
 * Created by evanalmonte on 12/8/17.
 */

public interface StepCounter {
    void notiftyListeners();

    interface StepCounterCallback {
        void onStepDetected();
    }
}
