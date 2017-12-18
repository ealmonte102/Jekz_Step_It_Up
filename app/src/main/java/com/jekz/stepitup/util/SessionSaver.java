package com.jekz.stepitup.util;

import com.jekz.stepitup.AvatarRepo;
import com.jekz.stepitup.data.LoginPreferences;
import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.model.step.Session;
import com.jekz.stepitup.model.step.SessionStepCounter;

/**
 * Created by evanalmonte on 12/18/17.
 */

public final class SessionSaver implements SessionStepCounter.SessionListener {

    private LoginPreferences sharedPrefs;
    private AvatarRepo avatarRepo = AvatarRepo.getInstance();


    public SessionSaver(LoginPreferences sharedPrefs) {
        this.sharedPrefs = sharedPrefs;
    }

    public void saveSession(Session session) {
        String sessionString = session.toString();

        if (sessionString.isEmpty()) { return; }
        String stepData = sharedPrefs.getString(SharedPrefsManager.Key.STEP_DATA, "");
        if (stepData.isEmpty()) {
            stepData = sessionString;
        } else {
            stepData = stepData + ";" + sessionString;
        }
        sharedPrefs.put(SharedPrefsManager.Key.STEP_DATA, stepData);
    }

    public synchronized boolean startStoringSession() {
        if (sharedPrefs.getBoolean(SharedPrefsManager.Key.COUNTING, false)) {
            return false;
        }
        sharedPrefs.put(SharedPrefsManager.Key.COUNTING, true);
        return true;
    }


    public synchronized boolean stopStoringSession() {
        if (sharedPrefs.getBoolean(SharedPrefsManager.Key.COUNTING, false)) {
            sharedPrefs.remove(SharedPrefsManager.Key.COUNTING,
                    SharedPrefsManager.Key.STEPS_COUNTED);
            return true;
        }
        return false;
    }

    public int getCurrentSteps() {
        return sharedPrefs.getInt(SharedPrefsManager.Key.STEPS_COUNTED, 0);
    }

    public boolean isStoringSteps() {
        return sharedPrefs.getBoolean(SharedPrefsManager.Key.COUNTING, false);
    }

    public void storeSteps(int stepsToStore) {
        sharedPrefs.put(SharedPrefsManager.Key.STEPS_COUNTED, stepsToStore);
    }

    @Override
    public void sessionEnded(Session session) {
        saveSession(session);
    }

    @Override
    public void onStepCountIncreased(int stepcount) {
        storeSteps(stepcount);
    }
}