package com.jekz.stepitup.model.step;

/**
 * Created by evanalmonte on 12/8/17.
 */


/**
 * Interface definition for a Step Counter
 */
public interface StepCounter {
    /**
     * @param numOfSteps The number of steps counted.
     */
    void notiftyListeners(int numOfSteps);

    /**
     * Add listener to set of listeners to be notified
     *
     * @param listener listener to be added
     */
    void addListener(StepCounterCallback listener);

    /**
     * Remove listener from set of listeners to be notified
     *
     * @param listener listener to be removed
     */
    void removeListener(StepCounterCallback listener);

    /**
     * Method which begins counting steps internally
     */
    void registerSensor();

    /**
     * Method which stops counting steps
     *
     * Calls sessionEnded on for each listener
     * session
     */
    void unregisterSensor();

    void startSession();

    void endSession();

    /**
     * Interface representing a callback which is called when a step is detected
     */
    interface StepCounterCallback {
        /**
         * Sends the number of steps taken to the callback
         *
         * @param numOfSteps The number of steps counted
         */
        void onStepDetected(int numOfSteps);
    }
}
