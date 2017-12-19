package com.jekz.stepitup.util;

import android.util.Log;

import com.jekz.stepitup.AvatarRepo;
import com.jekz.stepitup.data.LoginPreferences;
import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.data.request.SessionRequest;
import com.jekz.stepitup.model.step.Session;
import com.jekz.stepitup.model.step.SessionStepCounter;

import java.util.regex.Pattern;

/**
 * Created by evanalmonte on 12/18/17.
 */

public final class SessionSaver implements SessionStepCounter.SessionListener {
    private static final String TAG = SessionSaver.class.getName();

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
            stepData = sessionString + ";";
        } else {
            stepData = stepData + sessionString + ";";
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

    public void sendStoredSessions() {
        String sessions = sharedPrefs.getString(SharedPrefsManager.Key.STEP_DATA);
        if (sessions == null || sessions.isEmpty()) { return; }
        String[] sessionArray = sessions.split(";");
        Log.d(TAG, "Stored sessions: " + sessions);
        String[] splitSession;
        String startDate;
        String endDate;
        int totalNumOfSteps;
        for (String aSessionArray : sessionArray) {
            splitSession = aSessionArray.split(",");
            startDate = splitSession[0];
            endDate = splitSession[1];
            totalNumOfSteps = Integer.parseInt(splitSession[2]);
            sendRequest(startDate, endDate, totalNumOfSteps, aSessionArray);
        }
    }

    private void sendRequest(String startDate, String endDate, int stepCount, final String
            sessionToRemove) {
        final String session = sharedPrefs.getString(SharedPrefsManager.Key.SESSION);
        if (session.isEmpty()) { return; }
        SessionRequest request = new SessionRequest(session, startDate, endDate, stepCount, new
                SessionRequest.SessionRequestCallback() {
                    @Override
                    public void processResultOfSessionRequest(SessionRequest.SessionRequestResult
                                                                      result) {
                        switch (result) {
                            case SESSION_SAVED:
                                removeSessionFromPrefs(sessionToRemove);
                                break;
                            case UNSUCCESSFUL:
                                Log.d(TAG, "Error, please check formatting of request");
                                break;
                            case NETWORK_ERROR:
                                Log.d(TAG, "Network error, saving session");
                                break;
                        }
                    }
                });
        request.execute("https://jekz.herokuapp.com/api/db/update");
    }

    private void removeSessionFromPrefs(String sessionToRemove) {
        String session = sharedPrefs.getString(SharedPrefsManager.Key.STEP_DATA);
        String newStoredSessions = session.replaceFirst(Pattern.quote(sessionToRemove + ";"), "");
        Log.d(TAG, newStoredSessions);
        sharedPrefs.put(SharedPrefsManager.Key.STEP_DATA, newStoredSessions);
    }
}