package com.jekz.stepitup.model.step;

import java.util.Date;

/**
 * Created by evanalmonte on 12/9/17.
 */

public class Session {
    public final long startTime;
    public final long endTime;
    public final int totalSteps;

    public Session(long startTime, long endTime, int totalSteps) {
        if (startTime < 0 || endTime < 0 || totalSteps < 0) {
            throw new IllegalArgumentException();
        }
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalSteps = totalSteps;
    }

    @Override
    public String toString() {
        String startDate = new Date(startTime).toString();
        String endDate = new Date(endTime).toString();
        return startDate + " - " + endDate + " : " + String.valueOf(totalSteps) + " step(s) walked";
    }
}
