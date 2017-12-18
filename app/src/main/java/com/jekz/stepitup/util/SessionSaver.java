package com.jekz.stepitup.util;

import com.jekz.stepitup.data.LoginPreferences;
import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.model.step.Session;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        String sessionString = session.toString();

        if (sessionString.isEmpty()) { return; }
        String stepData = preferences.getString(SharedPrefsManager.Key.STEP_DATA, "");
        if (stepData.isEmpty()) {
            stepData = sessionString;
        } else {
            stepData = stepData + ";" + sessionString;
        }
        preferences.put(SharedPrefsManager.Key.STEP_DATA, stepData);
    }
}
