package com.jekz.stepitup.util;

import com.jekz.stepitup.data.LoginPreferences;
import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.model.step.Session;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by evanalmonte on 12/18/17.
 */

public final class SessionSaver {
    private static final DateFormat df = new SimpleDateFormat("yyyy-dd-MM hh:mm:ss ZZZZZ",
            Locale.US);

    private static final DateFormat dayFormat = new SimpleDateFormat("E", Locale.US);

    static {
        df.setTimeZone(TimeZone.getTimeZone("EST"));
        dayFormat.setTimeZone(TimeZone.getTimeZone("EST"));
    }

    public static void saveSession(Session session, LoginPreferences preferences) {
        long startTime = session.startTime;
        Date startDate = new Date(startTime);
        String sessionString = session.toString();
        switch (dayFormat.format(startDate)) {
            case "Mon":
                preferences.put(SharedPrefsManager.Key.STEP_DATA_MON, sessionString);
                break;
            case "Tue":
                preferences.put(SharedPrefsManager.Key.STEP_DATA_TUE, sessionString);
                break;
            case "Wed":
                preferences.put(SharedPrefsManager.Key.STEP_DATA_WED, sessionString);
                break;
            case "Thu":
                preferences.put(SharedPrefsManager.Key.STEP_DATA_THU, sessionString);
                break;
            case "Fri":
                preferences.put(SharedPrefsManager.Key.STEP_DATA_FRI, sessionString);
                break;
            case "Sat":
                preferences.put(SharedPrefsManager.Key.STEP_DATA_SAT, sessionString);
                break;
            case "Sun":
                preferences.put(SharedPrefsManager.Key.STEP_DATA_SUN, sessionString);
                break;
        }
    }
}
