package com.jekz.stepitup.model.step;

/**
 * Created by evanalmonte on 12/10/17.
 */

public interface IntervalStepCounter extends StepCounter {
    void setInterval(int numOfSeconds);

    void notifySessionEnded(Session session);

    void startAutoCount();
}
