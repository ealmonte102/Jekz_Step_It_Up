package com.jekz.stepitup.util;

import com.jekz.stepitup.data.LoginPreferences;
import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.model.step.Session;

/**
 * Created by evanalmonte on 12/18/17.
 */

public final class SessionSaver {
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
