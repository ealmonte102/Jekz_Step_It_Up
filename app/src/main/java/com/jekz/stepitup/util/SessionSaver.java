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
                saveToDate(SharedPrefsManager.Key.STEP_DATA_MON, sessionString, preferences);
                break;
            case "Tue":
                saveToDate(SharedPrefsManager.Key.STEP_DATA_TUE, sessionString, preferences);
                break;
            case "Wed":
                saveToDate(SharedPrefsManager.Key.STEP_DATA_WED, sessionString, preferences);
                break;
            case "Thu":
                saveToDate(SharedPrefsManager.Key.STEP_DATA_THU, sessionString, preferences);
                break;
            case "Fri":
                saveToDate(SharedPrefsManager.Key.STEP_DATA_FRI, sessionString, preferences);
                break;
            case "Sat":
                saveToDate(SharedPrefsManager.Key.STEP_DATA_SAT, sessionString, preferences);
                break;
            case "Sun":
                saveToDate(SharedPrefsManager.Key.STEP_DATA_SUN, sessionString, preferences);
                break;
        }
    }

    private static void saveToDate(SharedPrefsManager.Key dateKey, String sessionString,
                                   LoginPreferences preferences) {
        if (sessionString.isEmpty()) { return; }
        String stepData = preferences.getString(dateKey, "");
        if (stepData.isEmpty()) {
            stepData = sessionString;
        } else {
            stepData = stepData + ";" + sessionString;
        }
        preferences.put(dateKey, stepData);
    }
}
