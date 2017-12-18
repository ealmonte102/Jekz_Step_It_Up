package com.jekz.stepitup.model.step;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by evanalmonte on 12/9/17.
 */

public class Session {
    private static final DateFormat df = new SimpleDateFormat("yyyy-dd-MM hh:mm:ss ZZZZZ",
            Locale.US);

    static {
        df.setTimeZone(TimeZone.getTimeZone("EST"));
    }

    public final long startTime;
    public final long endTime;
    public final int totalSteps;

    public Session(long startTime, long endTime, int totalSteps) {
        if (startTime < 0) {
            throw new IllegalArgumentException("Start time is less than 0");
        } else if (endTime < 0) {
            throw new IllegalArgumentException("End time is less than 0");
        } else if (totalSteps < 0) {
            throw new IllegalArgumentException("Total steps are less than 0");
        }
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalSteps = totalSteps;
    }

    @Override
    public String toString() {
        Date startDate = new Date(startTime);
        Date endDate = new Date(endTime);
        return df.format(startDate) + "," + df.format(endDate) + "," + totalSteps;
    }
}
